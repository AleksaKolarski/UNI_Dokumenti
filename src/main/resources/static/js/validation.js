/* Input field validation:
** -validation for text field
** -validation for email field
** -validation for password reset/registration fields
**
** Author: Aleksa Kolarski (SF 27/2016)
** 2018
*/

// PUBLIC
// Add validation to text input field
function add_validation_text(field, min, max) {
    $(field).keyup(function (e) {
        _check_text(field, min, max);
    });
}

// Add validation to email input field
function add_validation_email(field, max) {
    $(field).keyup(function (e) {
        _check_email(field, max);
    });
}

// Add validation to two password input fields that need to match
function add_validation_password_match(field1, field2) {
    $(field1).keyup(function (e) {
        _check_password_match(field1, field2);
    });
    $(field2).keyup(function (e) {
        _check_password_match(field1, field2);
    });
}

// PRIVATE
// validacija input polja
function _check_text(field, min, max) {
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

// validacija email input polja
function _check_email(field, max) {
    if (/.+\@.+\..+/.test(field.val().toLowerCase()) && field.val().length <= max) {
        field.css('border', '1px solid #ccc');
        return true;
    }
    field.css('border', '1px solid #ff0000');
    return false;
}

function _check_password_match(field1, field2) {
    if (field1.val() == field2.val()) {
        field2.css('border', '1px solid #ccc');
        return true;
    }
    field2.css('border', '1px solid #ff0000');
    return false;
}