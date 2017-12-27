<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Adding new teacher</title>
</head>
<body>
    <form name="teacher" action="/admin/new_user" method="post">
        <p>Name:</p>
        <input title="Name" type="text" name="username"/>
        <p>Email:</p>
        <input title="Email" type="email" name="email"/>
        <p>Password:</p>
        <input title="Password" type="password" name="password"/>
        <p>Select user role :</p>
        <div>
            <input type="radio" id="contactChoice1"
                   name="user_role" value="2">
            <label for="contactChoice1">Student</label>

            <input type="radio" id="contactChoice2"
                   name="user_role" value="3">
            <label for="contactChoice2">Teacher</label>

            <input type="radio" id="contactChoice3"
                   name="user_role" value="6">
            <label for="contactChoice3">Admin</label>
        </div>
        <input type="submit" value="OK">
    </form>

</body>
</html>