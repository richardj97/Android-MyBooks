package uk.ac.lincoln.a16629926student.booksapi;

public class Utils {
    public static String ParseArray(String Str){
        String step1 = Str.replace("[", ""); // Remove [
        String step2 = step1.replace("]", ""); // Remove ]
        String step3 = step2.replace("\"", ""); // Remove ""
        String step4 = step3.replace(",", ", "); // Gives a space between
        return step4;
    }
}
