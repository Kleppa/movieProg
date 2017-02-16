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
// TODO: 16/02/2017 fix spaces in search for movie
public class Client {

    private ArrayList<String> jsonArr=new ArrayList<String>();
    private Scanner sc;
    private String movieTitle;
    private final String site="http://www.omdbapi.com/?";
    private ArrayList<String> unparsedJson=new ArrayList<String>();
    private String[] infoContent={"Title","Year","Rated","Released","Runtime", "Genre","Director","Writer",
    "Actors","Plot","Language","Country","Awards","Metascore","imdbRating","imdbVotes"};
    Client(){
        sc = new Scanner(System.in);

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

        int choice=99;

        while(choice!=0) {
            System.out.println("Please enter a choice");
            menuChoice();

            //problem with choice taking all the input
            choice = Integer.parseInt(sc.nextLine());


            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    movieInfo();
                    break;
                case 3:
                    compareMovies();
                    unparsedJson.clear();
                    jsonArr.clear();

                    break;
            }

        }
    }

    private String compareMovies() {
        String metaResult = "";
        String imdbResult = "";

        int metascore = 0;
        double imdbRating = 0;

        int i =0;
        for (String jObj : jsonArr) {

            JSONObject tmpJson = new JSONObject(unparsedJson.get(i));

            try {


            if (tmpJson.getString(infoContent[13]).equalsIgnoreCase("N/A")) {
                System.out.println(tmpJson.getString(infoContent[0]) + " Has no metascore");
            } else if (Integer.parseInt(tmpJson.getString(infoContent[13])) > metascore) {

                metascore = Integer.parseInt(tmpJson.getString(infoContent[13]));
                metaResult = tmpJson.getString(infoContent[0]) + " has the highest metascore with " + metascore;

                if (Double.parseDouble(tmpJson.getString(infoContent[14])) > imdbRating) {

                    imdbRating = Double.parseDouble(tmpJson.getString(infoContent[14]));
                    imdbResult = " and " + tmpJson.getString(infoContent[0]) + " has the highest imdb rating with " + imdbRating;

                }


            }

        i++;
        }catch (Exception e){
                System.out.println(e.toString());
            }
        }
        System.out.println(metaResult+imdbResult);
        return metaResult + imdbResult;
    }

    public void menuChoice(){
        System.out.println();
        System.out.println("1 - Find Movie");
        System.out.println("2 - Info about Movie");
        System.out.println("3 - Compare Movies (Remember to find 2 Movies to compare)");
        System.out.println("0 - To Exit");

    }

    public void movieInfo(){
            //get riktig index
        if(!unparsedJson.isEmpty()) {
            JSONObject jsonObject = new JSONObject(unparsedJson.get(0));

            for (int i = 0; i < infoContent.length; i++) {
                System.out.println(jsonObject.getString(infoContent[i]));
            }
            System.out.println("\n\n");

        }else{
            System.out.println("Choose a movie first");
        }
    }

    public void addMovie() {

        System.out.println("Enter movie name");
        Scanner gs = new Scanner(System.in);
        movieTitle = typeChecker(gs.nextLine());

        try {
            unparsedJson.add(getHTML(this.movieTitle));
            jsonArr.add(getHTML(this.movieTitle));

        } catch (Exception e) {
            System.out.println("getHtml error ");
        }
    }
        @org.jetbrains.annotations.Contract(pure = true)
        private String typeChecker(String movieRequest){
            String improvedMovieRequest="";
            for(char c: movieRequest.toCharArray()){

               if(Character.isWhitespace(c)){
                   improvedMovieRequest+="20%";
               }else{
                   improvedMovieRequest+=c;

               }
            }

        return improvedMovieRequest;
    }

}
