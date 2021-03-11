package security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CredentialManager {
    private ObjectMapper mapper;
    private Map<String, ArrayList<String>> db;

    public CredentialManager() {
        mapper = new ObjectMapper();
        db = new HashMap<String, ArrayList<String>>();
    }

    public Access checkPerms(String user, String password) {
        Set<String> keys = db.keySet();
        for (String account: keys) {
            if (account.equals(user)) {
                if (db.get(account).get(0).equals(getMd5(password))) {
                    String perm = db.get(account).get(1);
                    if (perm.contains("GOD")) {
                        return Access.GOD;
                    }
                    else if (perm.contains("BUILDER")) {
                        return Access.BUILDER;
                    }
                    else if (perm.contains("VIEWER")) {
                        return Access.VIEWER;
                    }
                    else {
                        return Access.DENIED;
                    }
                }
                else {
                    return Access.DENIED; //incorrect password error
                }
            }
        }
        return Access.DENIED;
    }

    public void loadAccounts() {
        ObjectNode accountNode;
        try {
            JsonParser parser = mapper.getFactory().createParser(new File("./src/security/credentials.json"));
            accountNode = mapper.readTree(parser);
            parser.close();
        } catch (IOException e) {
            System.err.println("Cannot read file");
            return;
        }
        ArrayNode JSONAccounts = (ArrayNode) accountNode.get("accounts");
        for (JsonNode accountJSON : JSONAccounts) {
            String user = accountJSON.get("user").asText();
            ArrayList<String> newInput = new ArrayList<String>();
            newInput.add(accountJSON.get("pass").asText());
            newInput.add(accountJSON.get("access").asText());
            db.put(user, newInput);
        }
    }

    public void addAccount(String user, String password, String confirmPassword, Access access) throws IOException {
        if (!(password.equals(confirmPassword))) {
            return; //throw error (passwords don't match)
        }
        else if (db.containsKey(user)) {
            return; //throw error (user already exists)
        }
        else {
            ArrayList<String> newInput = new ArrayList<>();
            newInput.add(getMd5(password));
            newInput.add(access.toString());
            db.put(user, newInput);
        }
        updateAccounts();
    }

    private void updateAccounts() throws IOException {
        ObjectNode accountNode = mapper.createObjectNode();
        ArrayNode accountsList = mapper.createArrayNode();

        Set<String> keys = db.keySet();
        for (String user: keys) {
            accountsList.add(getAccountNode(user, db.get(user).get(0), db.get(user).get(1)));
        }

        accountNode.put("accounts", accountsList);

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("./credentials.json"), accountNode);
    }

    private ObjectNode getAccountNode(String user, String pass, String access) {
        ObjectNode account = mapper.createObjectNode();
        account.put("user", user);
        account.put("pass", pass);
        account.put("access", access);
        return account;
    }

    private static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
