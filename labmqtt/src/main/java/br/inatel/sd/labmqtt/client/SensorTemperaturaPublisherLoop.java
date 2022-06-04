package br.inatel.sd.labmqtt.client;

import java.util.Random;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SensorTemperaturaPublisherLoop {

    public static void main(String[] args) throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        IMqttClient publisher = new MqttClient(MyConstants.URI_BROKER, publisherId);

        MqttMessage msg = getTemperaturaSolo();
        msg.setQos(0);
        msg.setRetained(false);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);

        publisher.connect(options);

        Integer i = 0;
        while (i < 10) {
            msg = getTemperaturaSolo();
            msg.setQos(0);
            msg.setRetained(true);
            publisher.publish(MyConstants.TOPIC_1, msg);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i++;
            System.out.println("Publicando mensagem " + i);
        }

        publisher.disconnect();
    }

    public static MqttMessage getTemperaturaSolo() {

        Random r = new Random();
        double temperatura = 80 + r.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f", temperatura).getBytes();
        return new MqttMessage(payload);
    }
}
