package myapps;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class Pipe {
    public static void main(String[] args) throws Exception{
        Properties props = new Properties();
        // kafkaライブラリを入れた時点で、StreamsConfigが設定されている。
        // そこに設定を加えている
        // streamを一意に識別する
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        // kafkaサーバ
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // シリアライズするライブラリを指定
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    }
}
