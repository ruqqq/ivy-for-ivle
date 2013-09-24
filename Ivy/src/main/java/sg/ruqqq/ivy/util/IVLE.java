package sg.ruqqq.ivy.util;

/**
 * Created by ruqqq on 23/9/13.
 */
public class IVLE {
    public static String API_KEY = "";

    public static String BASE_URL = "https://ivle.nus.edu.sg/api/";
    public static String BASE_API_URL = BASE_URL + "Lapi.svc/";
    public static String LOGIN_URL = BASE_URL + "login/?apikey=" + API_KEY + "&url=https://ivle.nus.edu.sg";

    public static class Login {
        // return ValidateResp
        public static String VALIDATE_URL(String token) { return BASE_API_URL + "Validate?APIKey=" + API_KEY + "&Token=" + token; }

        public static class ValidateResp {
            String Token;
            boolean Success;
            String ValidTill;

            public String getToken() {
                return Token;
            }

            public boolean isSuccess() {
                return Success;
            }

            public long getValidTill() {
                return Long.parseLong(ValidTill.replace("/Date(", "").replace("+0800)/", ""));
            }
        }

        // return string
        public static String USERNAME_GET_URL(String token) { return BASE_API_URL + "UserName_Get?APIKey=" + API_KEY + "&Token=" + token; }

        // return string
        public static String USERID_GET_URL(String token) { return BASE_API_URL + "UserID_Get?APIKey=" + API_KEY + "&Token=" + token; }

        // return string
        public static String USEREMAIL_GET_URL(String token) { return BASE_API_URL + "UserEmail_Get?APIKey=" + API_KEY + "&Token=" + token; }
    }

    public static class MyOrganizer {
        // returns array of Timetable
        public static String TIMETABLE_STUDENT_URL(String token, String acadYear, String semester) { return BASE_API_URL + "Timetable_Student?APIKey=" + API_KEY + "&AuthToken=" + token + "&AcadYear=" + acadYear + "&Semester=" + semester; }
    }
}
