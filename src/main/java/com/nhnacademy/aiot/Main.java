package com.nhnacademy.aiot;

import com.nhnacademy.aiot.node.ActiveNode;
import com.nhnacademy.aiot.node.Inable;
import com.nhnacademy.aiot.node.Outable;
import com.nhnacademy.aiot.wire.Wire;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String SETTING_FILENAME = "settings.json";

    private static final CommandLineParser parser = new BasicParser();

    private static Options getOptions() {
        Options options = new Options();
        options.addOption("an", true, "--an {ApplicationId}");
        options.addOption("s", "sensor", true, "-s {Sensor 이름, ...}");
        options.addOption("c", false, "-c");
        return options;
    }

    public static void main(String[] args) {
        try {
            String resourceFile = Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource(SETTING_FILENAME)).getFile();

            Map<String, ActiveNode> activeNodes = new HashMap<>();
            for (Object obj : new JSONArray(Files.readString(Paths.get(resourceFile)))) {
                ActiveNode activeNode = makeActiveNode((JSONObject) obj, args);
                activeNodes.put(activeNode.getId(), activeNode);
            }

            connectWire(activeNodes);

            for (ActiveNode activeNode : activeNodes.values()) {
                activeNode.start();
            }
        } catch (IOException | ClassNotFoundException | InvocationTargetException |
                 InstantiationException | IllegalAccessException | NoSuchMethodException |
                 ParseException e) {
            LOGGER.error(e.getMessage());
        }

    }

    private static void connectWire(Map<String, ActiveNode> activeNodes) {
        for (ActiveNode activeNode : activeNodes.values()) {
            if (activeNode instanceof Inable) {
                Inable node = (Inable) activeNode;
                for (int i = 0; i < node.getOutWireCount(); i++) {
                    Wire wire = node.getOutWire(i);
                    ((Outable) activeNodes.get(wire.getId())).addInWire(wire);
                }
            }
        }

    }

    private static ActiveNode makeActiveNode(JSONObject jsonObject, String[] args)
        throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, ParseException {

        List<Object> objs = makeConstructorArgs(jsonObject, args);

        Class<?> clazz = Class.forName("com.nhnacademy.aiot.node." + jsonObject.getString("type"));
        ActiveNode node = (ActiveNode) clazz.getConstructors()[0].newInstance(objs.toArray());

        JSONArray wires = jsonObject.getJSONArray("wires");
        if (!wires.isEmpty()) {
            wires = wires.getJSONArray(0);
            for (int i = 0; i < wires.length(); i++) {
                clazz.getMethod("addOutWire", Wire.class)
                    .invoke(node, new Wire((String) wires.get(i)));
            }
        }

        return node;
    }

    private static List<Object> makeConstructorArgs(JSONObject jsonObject, String[] args) throws ParseException {
        List<Object> objs = new ArrayList<>();
        objs.add(jsonObject.getString("id"));
        objs.add(jsonObject.getString("name"));

        if (jsonObject.has("broker") && jsonObject.has("port")) {
            objs.add(String.format("tcp://%s:%s", jsonObject.getString("broker"), jsonObject.getString("port")));
        }
        if (jsonObject.has("topic")) {
            objs.add(jsonObject.getString("topic"));
        }

        if (jsonObject.has("options")) {
            CommandLine commandLine = parser.parse(getOptions(), args);
            if (commandLine.hasOption("c")) {
                objs.add(commandLine);
            } else {
                List<String> list = new ArrayList<>();
                JSONArray array = jsonObject.getJSONArray("options");
                for (int i = 0; i < array.length(); i++) {
                    list.add("--" + array.getString(i));
                    list.add(jsonObject.getString(array.getString(i)));
                }
                objs.add(parser.parse(getOptions(), list.toArray(String[]::new)));
            }
        }

        return objs;
    }
}
