package com.kleppa.movieProgram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.*;

/**
 * Created by Kleppa on 14/02/2017.
 */
public class Client {
    private Scanner sc;
    private String movieTitle=null;
    private final String site="http://www.omdbapi.com/?";
    private ArrayList<String> unparsedJson=new ArrayList<String>();
    private String[] infoContent={"Title","Year","Rated","Released","Runtime", "Genre","Director","Writer",
    "Actors","Plot","Language","Country","Awards","Metascore","imdbRating","imdbVotes"};

    Client(String movieTitle){
     this.movieTitle = movieTitle;
        try {
            unparsedJson.add(getHTML(this.movieTitle));
        }catch (Exception e){
            System.out.println("getHtml error");
        }
        menu();
    }

    public String getHTML(String movieTitle) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(site+"t="+movieTitle);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public void menu(){
        menuChoice();;
        sc = new Scanner(System.in);
        int choice=sc.nextInt();
        switch (choice){
            case 1:
                movieInfo();
                break;
            case 2:
                compareMovies();
                break;
        }

    }

    private void compareMovies() {
        try {
            unparsedJson.add(getHTML(sc.nextLine()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void menuChoice(){
        System.out.println("1 - About Movie");
        System.out.println("2 - Compare Movies");

    }
    public void movieInfo(){
            //get riktig index

        JSONObject jsonObject = new JSONObject(unparsedJson.get(0));

        for(int i =0; i<infoContent.length;i++){
        System.out.println(jsonObject.getString(infoContent[i]));
            }
    }
}
