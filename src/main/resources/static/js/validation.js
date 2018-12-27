/* Input field validation:
** -validation for text field
** -validation for email field
** -validation for password reset/registration fields
** 
** -use add_validation_* for continous check
** -use check_* for on demand check
**
** Author: Aleksa Kolarski (SF 27/2016)
** 2018
*/

// Add validation to text input field
function add_validation_text(field, min, max) {
    $(field).keyup(function (e) {
        check_text(field, min, max);
    });
}

// Add validation to email input field
function add_validation_email(field, max) {
    $(field).keyup(function (e) {
        check_email(field, max);
    });
}

// Add validation to two password input fields that need to match
function add_validation_password_match(field1, field2) {
    $(field1).keyup(function (e) {
        check_password_match(field1, field2);
    });
    $(field2).keyup(function (e) {
        check_password_match(field1, field2);
    });
}

// ... plus min & max length
function add_validation_password_match(field1, field2, min, max) {
    $(field1).keyup(function (e) {
        check_password_match(field1, field2, min, max);
    });
    $(field2).keyup(function (e) {
        check_password_match(field1, field2, min, max);
    });
}

// One time check
function check_text(field, min, max) {
    var content_length = field.val().length;
    if (content_length >= min && content_length <= max) {
        field.css('border-style', 'solid');
        field.css('border-color', '#ccc');
        return true;
    }
    field.css('border-style', 'solid');
    field.css('border-color', '#f00');
    return false;
}

// One time check
function check_email(field, max) {
    if (/.+\@.+\..+/.test(field.val().toLowerCase()) && field.val().length <= max) {
        field.css('border', '1px solid #ccc');
        return true;
    }
    field.css('border', '1px solid #ff0000');
    return false;
}

// One time check
function check_password_match(field1, field2) {
    if (field1.val() == field2.val()) {
        field2.css('border', '1px solid #ccc');
        return true;
    }
    field2.css('border', '1px solid #ff0000');
    return false;
}

// ... plus min & max length
function check_password_match(field1, field2, min, max) {
    var content_length = field1.val().length;
    if (field1.val() == field2.val() && content_length >= min && content_length <= max) {
        field2.css('border', '1px solid #ccc');
        return true;
    }
    field2.css('border', '1px solid #ff0000');
    return false;
}