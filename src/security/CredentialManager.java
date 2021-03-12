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
    // This map stores the accounts in memory
    // Its format is <Username, [Password, Permission Level]>
    private Map<String, ArrayList<String>> db;

    private final static String CREDENTIALS_PATH = "./src/security/credentials.json";

    public CredentialManager() {
        mapper = new ObjectMapper();
        db = new HashMap<>();
    }

    // Checks if the input username and password strings match an existing account
    // Returns the account's permission level if it exists, returns DENIED if it does not exist
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
                    return Access.DENIED; // incorrect password error
                }
            }
        }
        return Access.DENIED;
    }

    // Loads the accounts into memory from the JSON file
    public void loadAccounts() {
        ObjectNode accountNode;
        try {
            JsonParser parser = mapper.getFactory().createParser(new File(CREDENTIALS_PATH));
            accountNode = mapper.readTree(parser);
            parser.close();
        } catch (IOException e) {
            System.err.println("Cannot read file");
            return;
        }
        ArrayNode JSONAccounts = (ArrayNode) accountNode.get("accounts");
        for (JsonNode accountJSON : JSONAccounts) {
            String user = accountJSON.get("user").asText();
            ArrayList<String> newInput = new ArrayList<>();
            newInput.add(accountJSON.get("pass").asText());
            newInput.add(accountJSON.get("access").asText());
            db.put(user, newInput);
        }
    }

    // Adds an account to the accounts stored in memory and writes it to the JSON file
    // Returns GRANTED if successful, other errors possible if unsuccessful
    public AccountResponse addAccount(String user, String password, String confirmPassword, Access access) throws IOException {
        if (!(password.equals(confirmPassword))) {
            return AccountResponse.PASSNOMATCHERR; //throw error (passwords don't match)
        }
        else if (db.containsKey(user)) {
            return AccountResponse.USEREXISTSERR; //throw error (user already exists)
        } else if (!user.matches("[A-Za-z0-9]+")) {
            return AccountResponse.INVALIDNAME;
        }
        else {
            ArrayList<String> newInput = new ArrayList<>();
            newInput.add(getMd5(password));
            newInput.add(access.toString());
            db.put(user, newInput);
        }
        updateAccounts();
        return AccountResponse.GRANTED;
    }

    // Tries to delete the account with matching username from the database
    // Returns true if successful, false if unsuccessful
    public boolean deleteAccount(String username) {
        if (db.remove(username) == null) {
            // This account was not found
            return false;
        }

        try {
            updateAccounts();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Returns a list of "username - permission level" strings
    public List<String> getUserItems() {
        List<String> usernames = new ArrayList<>();
        db.entrySet().forEach(entry -> {
            String item = entry.getKey();
            item = item + " - " + entry.getValue().get(1);
            usernames.add(item);
        });
        return usernames;
    }

    // Writes the accounts stored in memory into the credentials.json file
    private void updateAccounts() throws IOException {
        ObjectNode accountNode = mapper.createObjectNode();
        ArrayNode accountsList = mapper.createArrayNode();

        Set<String> keys = db.keySet();
        for (String user: keys) {
            accountsList.add(getAccountNode(user, db.get(user).get(0), db.get(user).get(1)));
        }

        accountNode.put("accounts", accountsList);

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(CREDENTIALS_PATH), accountNode);
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
