package auth.domain.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @author João Wolff <1200049@isep.ipp.pt> inspired in class by: Paulo Maio <pam@isep.ipp.pt>
 */
public class Email{

    private final String email;

    public Email(String email)
    {
        if (!validate(email))
            throw new IllegalArgumentException("Invalid Email Address.");
        this.email = email;
    }

    private boolean validate(String email) {
        if (StringUtils.isBlank(email))
            return false;
        return checkFormat(email);
    }

    // Extracted from https://www.geeksforgeeks.org/check-email-address-valid-not-java/
    private boolean checkFormat(String email)
    {
        String emailRegex = "^[\\w-']+(\\.[\\w-']+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public String getEmail(){
        return email;
    }

    @Override
    public boolean equals(Object o) {
        // Inspired in https://www.sitepoint.com/implement-javas-equals-method-correctly/

        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        // field comparison
        Email obj = (Email) o;
        return Objects.equals(this.email, obj.email);
    }

    @Override
    public String toString()
    {
        return String.format("%s", this.email);
    }
}
