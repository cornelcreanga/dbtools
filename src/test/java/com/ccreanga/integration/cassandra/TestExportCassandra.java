package com.ccreanga.integration.cassandra;

import com.ccreanga.RandomUtil;
import com.ccreanga.UUIDGen;
import com.datastax.driver.core.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

import static com.ccreanga.RandomUtil.generateDate;


public class TestExportCassandra {

    private static Session session;
    private static Cluster cluster;

    @Before
    public void staticSetup() {
        cluster = null;
        cluster = Cluster.builder()
                .addContactPoint("127.0.0.1")
                .build();
        session = cluster.connect();

        KeyspaceMetadata keyspaceMetadata = cluster.getMetadata().getKeyspace("test");
        if (keyspaceMetadata == null) {
            session.execute("CREATE KEYSPACE test\n" +
                    "WITH durable_writes = true\n" +
                    "AND replication = {\n" +
                    "\t'class' : 'SimpleStrategy',\n" +
                    "\t'replication_factor' : 1\n" +
                    "};\n");
            session.execute("CREATE TABLE test.test_types (\n" +
                    "  id uuid PRIMARY KEY,\n" +
                    "  c_ascii text,\n" +
                    "  c_bigint bigint,\n" +
                    "  c_blob blob,\n" +
                    "  c_boolean boolean,\n" +
                    "  c_date date,\n" +
                    "  c_decimal decimal,\n" +
                    "  c_double double,\n" +
                    "  c_float float,\n" +
                    "  c_inet inet,\n" +
                    "  c_int int,\n" +
                    "  c_list list<text>,\n" +
                    "  c_map map<text,text>,\n" +
                    "  c_set set<text>,\n" +
                    "  c_smallint smallint,\n" +
                    "  c_text text,\n" +
                    "  c_time time,\n" +
                    "  c_timestamp timestamp,\n" +
                    "  c_timeuuid timeuuid,\n" +
                    "  c_tinyint tinyint,\n" +
                    "  c_varchar varchar,\n" +
                    "  c_varint varint\n" +
                    ");");

        } else {
            TableMetadata tableMetadata = keyspaceMetadata.getTable("test_types");
            if (tableMetadata == null) {
                session.execute("CREATE TABLE test.test_types (\n" +
                        "  id uuid PRIMARY KEY,\n" +
                        "  c_ascii text,\n" +
                        "  c_bigint bigint,\n" +
                        "  c_blob blob,\n" +
                        "  c_boolean boolean,\n" +
                        "  c_date date,\n" +
                        "  c_decimal decimal,\n" +
                        "  c_double double,\n" +
                        "  c_float float,\n" +
                        "  c_inet inet,\n" +
                        "  c_int int,\n" +
                        "  c_list list<text>,\n" +
                        "  c_map map<text,text>,\n" +
                        "  c_set set<text>,\n" +
                        "  c_smallint smallint,\n" +
                        "  c_text text,\n" +
                        "  c_time time,\n" +
                        "  c_timestamp timestamp,\n" +
                        "  c_timeuuid timeuuid,\n" +
                        "  c_tinyint tinyint,\n" +
                        "  c_varchar varchar,\n" +
                        "  c_varint varint\n" +
                        ");");

            }
        }

        ResultSet rs = session.execute("select release_version from system.local");
        Row row = rs.one();
        System.out.println(row.getString("release_version"));

    }

    @After
    public void staticTearDown() throws Exception {
        if (cluster != null)
            cluster.close();
    }

    @Test
    public void testExport() throws UnknownHostException {
        PreparedStatement ps = session.prepare("insert into test.test_types(id,\n" +
                "  c_ascii,\n" +
                "  c_bigint,\n" +
                "  c_blob,\n" +
                "  c_boolean,\n" +
                "  c_date,\n" +
                "  c_decimal,\n" +
                "  c_double,\n" +
                "  c_float,\n" +
                "  c_inet,\n" +
                "  c_int,\n" +
                "  c_list,\n" +
                "  c_map,\n" +
                "  c_set,\n" +
                "  c_smallint,\n" +
                "  c_text,\n" +
                "  c_time,\n" +
                "  c_timestamp,\n" +
                "  c_timeuuid,\n" +
                "  c_tinyint,\n" +
                "  c_varchar,\n" +
                "  c_varint) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        List<String> list = Arrays.asList("element1", "element2");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        Set<String> set = new HashSet<>();
        set.add("set1");
        set.add("set2");

        Random random = new Random();

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {

            BoundStatement bound = ps.bind()
                    .setUUID("id", UUID.randomUUID())
                    .setString("c_ascii", RandomUtil.generateString(12))
                    .setLong("c_bigint", random.nextLong())
                    .setBytes("c_blob", ByteBuffer.wrap(RandomUtil.generateBytes(4000)))
                    .setBool("c_boolean", true)
                    .setDate("c_date", LocalDate.fromMillisSinceEpoch(RandomUtil.generateDate()))
                    .setDecimal("c_decimal", new BigDecimal(Double.MAX_VALUE * Math.random()))
                    .setDouble("c_double", Double.MAX_VALUE * random.nextDouble())
                    .setFloat("c_float", Float.MAX_VALUE + random.nextFloat())
                    .setInet("c_inet", InetAddress.getByName("127.0.0.1"))
                    .setInt("c_int", Integer.MAX_VALUE * random.nextInt())
                    .setList("c_list", list)
                    .setMap("c_map", map)
                    .setSet("c_set", set)
                    .setShort("c_smallint", (short) random.nextInt(Short.MAX_VALUE))
                    .setString("c_text", RandomUtil.generateString(4000))
                    .setTime("c_time", new Time(generateDate()).getTime())
                    .setTimestamp("c_timestamp", new Timestamp(generateDate()))
                    .setUUID("c_timeuuid", UUIDGen.getTimeUUIDFromMicros(System.currentTimeMillis()))
                    .setByte("c_tinyint", (byte) random.nextInt(Byte.MAX_VALUE))
                    .setString("c_varchar", RandomUtil.generateString(1024))
                    .setVarint("c_varint", new BigInteger(128, random));

            session.execute(bound);
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
    }
}
