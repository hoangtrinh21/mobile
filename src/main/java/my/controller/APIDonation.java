package my.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import my.models.Donation;
import spark.Spark;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class APIDonation {
    private static List<Donation> donationList = new LinkedList<>();
    private static final int PORT = 30000;
    public static void main(String[] args) {
        Spark.port(PORT);

        createTmpList();

        Spark.get("/donations", (request, response) -> {
            response.type("text/json");

            JsonArray jsonArray = new JsonArray();
            JsonElement element;
            for (int i = donationList.size() - 1; i >= 0; i--) {
                element = new Gson().toJsonTree(donationList.get(i));
                jsonArray.add(element);
            }

            return jsonArray;
        });

        Spark.post("/donations", (request, response) -> {
            Donation donation = new Gson().fromJson(request.body(), Donation.class);
            donationList.add(donation);
            return "DONE!";
        });

        Spark.delete("/donations", (request, response) -> {
            donationList.clear();
            return "DONE!";
        });
    }

    public static void createTmpList() {
        Donation donation = new Donation("1", 1000, "PayPal", 0);
        donationList.add(donation);
        donation = new Donation("2", 500, "Direct", 1);
        donationList.add(donation);
    }
}