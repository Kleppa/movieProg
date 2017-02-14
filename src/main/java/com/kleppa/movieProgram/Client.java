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
// TODO: 14/02/2017 fix scanner bullshit, fix gethtml error
public class Client {

    private ArrayList<JSONObject> jsonArr=new ArrayList<JSONObject>();
    private Scanner sc;
    private String movieTitle=null;
    private final String site="http://www.omdbapi.com/?";
    private ArrayList<String> unparsedJson=new ArrayList<String>();
    private String[] infoContent={"Title","Year","Rated","Released","Runtime", "Genre","Director","Writer",
    "Actors","Plot","Language","Country","Awards","Metascore","imdbRating","imdbVotes"};
    Client(){


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
        int choice=99;


        while(choice!=0) {
            System.out.println("Please enter a choice");
            //problem with choice taking all the input
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    movieInfo();
                    break;
                case 3:
                    compareMovies();
                    break;
            }

        }
    }

    private String compareMovies() {
        String metaResult="";
        String imdbResult="";

        int metascore=0;
        double imdbRating=0;
        for (JSONObject jObj:jsonArr) {

          if(Integer.parseInt(jObj.getString(infoContent[13]))>metascore){
                metascore=Integer.parseInt(jObj.getString(infoContent[13]));
                metaResult=jObj.getString(infoContent[0])+" has the highest metascore with " + metascore;

        }
            if(Double.parseDouble(jObj.getString(infoContent[14]))>imdbRating){
                imdbRating=Double.parseDouble(jObj.getString(infoContent[14]));
                imdbResult=" and "+ jObj.getString(infoContent[0]) + "has the highest imdb rating with " + imdbRating;

        }

        }
        System.out.println("Reutn values compareMovies "+metaResult+imdbResult);
        return metaResult+imdbResult;
    }

    public void menuChoice(){

        System.out.println("1 - Find Movie");
        System.out.println("2 - Info about Movie");
        System.out.println("3 - Compare Movies (Remember to find 2 Movies to compare)");
        System.out.println("0 - To Exit");

    }
    public void movieInfo(){
            //get riktig index
        if(!unparsedJson.isEmpty()) {
            JSONObject jsonObject = new JSONObject(unparsedJson.get(0));
            jsonArr.add(jsonObject);
            for (int i = 0; i < infoContent.length; i++) {
                System.out.println(jsonObject.getString(infoContent[i]));
            }
        }else{
            System.out.println("Choose a movie first");
        }
    }

    public void addMovie(){

        System.out.println("Enter movie name");
        Scanner mvsc=new Scanner(System.in);
        movieTitle=mvsc.nextLine();

        try {
            unparsedJson.add(getHTML(this.movieTitle));


        }catch (Exception e){
            System.out.println("getHtml error ");
        }
    }
}
