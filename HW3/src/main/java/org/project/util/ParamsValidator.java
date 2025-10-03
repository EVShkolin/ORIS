package org.project.util;

public class ParamsValidator {

    public String getErrorParams(String name, String email) {
        StringBuilder sb = new StringBuilder("?");

        if (name == null || name.isBlank()) {
            sb.append("nameError=Name should not be empty&");
        }

        if (email == null || email.isBlank() || !email.contains("@")) {
            sb.append("emailError=Invalid email");
        }

        if (sb.charAt(sb.length() - 1) == '&') {
            sb.deleteCharAt(sb.length() - 1);
        }

        if (sb.length() == 1)
            return "";

        return sb.toString();
    }

}
