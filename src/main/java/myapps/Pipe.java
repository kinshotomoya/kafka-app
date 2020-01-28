package myapps;

import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Pipe {
    public static void main(String[] args) throws Exception{
        Properties props = new Properties();
        // kafkaライブラリを入れた時点で、StreamsConfigが設定されている
        // そこに設定を加えている
        // streamを一意に識別する
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        // kafkaサーバ
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // シリアライズするライブラリを指定
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();
        // topic streams-plaintext-inputと接続するsource streamを作っている
        // 返り値として、key valueの値がstreams-plaintext-input topicから返ってくる
        // KStream<Object, Object> stream = builder.stream("streams-plaintext-input")
        // toまで一行で書ける
        // streams-plaintext-input topicから取得して、streams-pipe-output topicに挿入する
        builder.stream("streams-plaintext-input").to("streams-pipe-output");
        final Topology topology = builder.build();
        System.out.println(topology.describe());

        // kafka clientを生成する
        KafkaStreams streams = new KafkaStreams(topology, props);

        final CountDownLatch latch = new CountDownLatch(1);

        // control-cが押された場合にハンドリングする
        // streamは、startされたらclose()されるまでされ続けるので、ここでハンドリングしている
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            // stream開始
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
