package nhnacademy.aiot;

import nhnacademy.aiot.node.CLIFilterNode;
import nhnacademy.aiot.node.DebugNode;
import nhnacademy.aiot.node.DeviceTopicNode;
import nhnacademy.aiot.node.InNode;
import nhnacademy.aiot.node.InOutNode;
import nhnacademy.aiot.node.MQTTInNode;
import nhnacademy.aiot.node.MQTTOutNode;
import nhnacademy.aiot.node.OutNode;
import nhnacademy.aiot.wire.Wire;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static Options setOptions() {
        Options options = new Options();
        options.addOption("an", true, "Application Name");
        options.addOption("s", true, "Sensor");
        return options;
    }

    public static void main(String[] args) {
        Options options = setOptions();
        CommandLine commandLine;

        try {
            CommandLineParser parser = new BasicParser();
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            return;
        }

        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        Wire wire3 = new Wire();
        Wire wire4 = new Wire();

        InNode mqttInNode = new MQTTInNode("NHN Academy EMS", "tcp://ems.nhnacademy.com:1883", "application/#", 1);
        mqttInNode.setOutWire(0, wire1);

        InOutNode deviceTopicNode = new DeviceTopicNode("Device Topic Node", 1, 2);
        deviceTopicNode.setInWire(0, wire1);
        deviceTopicNode.setOutWire(0, wire2);
        deviceTopicNode.setOutWire(1, wire3);

        OutNode mqttInDebugNode = new DebugNode("Device Topic Debug Node", 1);
        mqttInDebugNode.setInWire(0, wire3);

        InOutNode cliNode = new CLIFilterNode("CLI Node", commandLine, 1, 1);
        cliNode.setInWire(0, wire2);
        cliNode.setOutWire(0, wire4);

        OutNode mqttOutNode = new MQTTOutNode("Localhost", "tcp://localhost:1883", 1);
        mqttOutNode.setInWire(0, wire4);

        mqttOutNode.start();
        cliNode.start();
        mqttInDebugNode.start();
        deviceTopicNode.start();
        mqttInNode.start();
    }
}
