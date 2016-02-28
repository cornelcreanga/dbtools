package com.ccreanga.anonymizer;

import java.io.BufferedReader;
import java.io.StringReader;

public class ICalAnonymizer implements Anonymizer {

    private int end(StringBuilder s,int start){
        char[] c =":;\"".toCharArray();
        int[] indices = new int[c.length];
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < indices.length; i++) {
            indices[i]=s.indexOf(""+c[i],start);
            if (indices[i]==-1)
                indices[i]=Integer.MAX_VALUE;
            if (indices[i]<min)
                min = indices[i];
        }

        if (min==Integer.MAX_VALUE)
            return s.length();
        return min;
    }

    @Override
    public Object anonymize(Object original) {
        String event = (String)original;
        BufferedReader reader = new BufferedReader(new StringReader(event));
        String line;
        StringBuilder result = new StringBuilder(event.length()+16);
        StringAnonymizer stringAnonymizer = new StringAnonymizer();
        try {
            while((line = reader.readLine())!=null){
                line = line.toUpperCase();
                if (line.startsWith("ORGANIZER") || line.startsWith("ATTENDEE")){

                    StringBuilder sb = new StringBuilder(line);

                    int start = 0;
                    while(true){
                        int index = sb.indexOf("CN=",start);
                        if (index==-1)
                            break;
                        if (sb.charAt(index+3)=='"')
                            index++;
                        int end = end(sb,index+3);
                        start = end;
                        sb.replace(index+3,end,stringAnonymizer.anonymize(end-index-3));
                    }

                    while(true){
                        int index = sb.indexOf("MAILTO:",start);
                        if (index==-1)
                            break;
                        int end = end(sb,index+7);
                        start = end;
                        sb.replace(index+7,end,stringAnonymizer.anonymize(end-index));
                    }
                    result.append(sb).append("\n");

                }else if (line.startsWith("SUMMARY") || line.startsWith("DESCRIPTION")){//replace after :
                    int index = line.indexOf(":");
                    if (index!=-1){
                        result.append(line.substring(0,index+1));
                        result.append(stringAnonymizer.anonymize(line.substring(index+1)));
                        result.append("\n");
                    }

                }else{
                    result.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            System.out.println(event);
            throw new RuntimeException(e);//should never happen
        }
        return result.toString();

    }

    public static void main(String[] args) {
        String s = "BEGIN:VCALENDAR \n" +
                "PRODID:-//1&1 Mail & Media GmbH/WEB.DE Kalender Server 2.14.0//NONSGML//DE \n" +
                "VERSION:2.0 \n" +
                "CALSCALE:GREGORIAN \n" +
                "BEGIN:VEVENT \n" +
                "ORGANIZER;CN=Service;SENT-BY=\"MAILTO:Ulrich.Gasch@gama-tronik.de\":MAILTO:service@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Dominik Knoll:MAILTO:dominik.knoll@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=\"Heinisch, Dennis\":MAILTO:dennis_heinisch@web.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Koeksal Baydogan:MAILTO:Koeksal.Baydogan@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Miguel Carvalho:MAILTO:Miguel.Carvalho@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=\"Morgenstern, Marco\":MAILTO:morgenstern-marco@web.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Ralf Roeske:MAILTO:ralf.roeske@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Reiner Schwartz:MAILTO:reiner.schwartz@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Rainer Schimmele:MAILTO:Rainer.Schimmele@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Stefan Reither:MAILTO:stefan.reither@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Thorsten Schwarz:MAILTO:thorsten.schwarz@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Tobias Decker:MAILTO:Tobias.Decker@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Service:MAILTO:service@gama-tronik.de \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=\"Vetter, Jan (jan.vetter@abbvie.com)\":MAILTO:jan.vetter@abbvie.com \n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=\"Arne Wörpel (arne.woerpel@abbvie.com)\":MAILTO:arne.woerpel@abbvie.com \n" +
                "DESCRIPTION;LANGUAGE=de-DE:Novec Display\\, Produktunterlagen\\, Teilnehmerliste etc. mitnehmen\\nTeilnahme ca. 15 Personen\\n\\n \n" +
                "SUMMARY;LANGUAGE=de-DE:AbbVie Novec-1230 Unterweisung/ Produktvorstellung\\, Geb. 51 Raum 105\\, Gasch-Carvalho \n" +
                "DTSTART;TZID=Europe/Vienna:20140512T090000 \n" +
                "DTEND;TZID=Europe/Vienna:20140512T120000 \n" +
                "UID:040000008200E00074C5B7101A82E008000000006006CA169642CF0100000000000000001000000081FF1D47A87EB54D90461FA041F7D8EB \n" +
                "CLASS:PUBLIC \n" +
                "PRIORITY:5 \n" +
                "DTSTAMP:20140318T094531Z \n" +
                "TRANSP:OPAQUE \n" +
                "STATUS:CONFIRMED \n" +
                "SEQUENCE:0 \n" +
                "LOCATION;LANGUAGE=de-DE:Ludwigshafen \n" +
                "X-MICROSOFT-CDO-APPT-SEQUENCE:0 \n" +
                "X-MICROSOFT-CDO-OWNERAPPTID:-1531824162 \n" +
                "X-MICROSOFT-CDO-BUSYSTATUS:TENTATIVE \n" +
                "X-MICROSOFT-CDO-INTENDEDSTATUS:BUSY \n" +
                "X-MICROSOFT-CDO-ALLDAYEVENT:FALSE \n" +
                "X-MICROSOFT-CDO-IMPORTANCE:1 \n" +
                "X-MICROSOFT-CDO-INSTTYPE:0 \n" +
                "BEGIN:VALARM \n" +
                "TRIGGER:-PT30M \n" +
                "UID:2f16755e-5b77-43ad-a4b4-5f9486698dbc \n" +
                "ACTION:X-EMAIL \n" +
                "DESCRIPTION:X-EMAIL \n" +
                "END:VALARM \n" +
                "END:VEVENT \n" +
                "END:VCALENDAR";
        ICalAnonymizer iCalAnonymizer = new ICalAnonymizer();
        System.out.println(iCalAnonymizer.anonymize("ORGANIZER;CN=Service;SENT-BY=\"MAILTO:Ulrich.Gasch@gama-tronik.de\":MAILTO:service@gama-tronik.de"));
//        System.out.println(iCalAnonymizer.anonymize("ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=Dominik Knoll:MAILTO:dominik.knoll@gama-tronik.de"));

        //System.out.println(iCalAnonymizer.anonymize(s));

        System.out.println(iCalAnonymizer.anonymize("ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE;CN=\"Vetter, Jan (jan.vetter@abbvie.com)\":MAILTO:jan.vetter@abbvie.com"));



        //System.out.println(iCalAnonymizer.anonymize(s));

    }
}
