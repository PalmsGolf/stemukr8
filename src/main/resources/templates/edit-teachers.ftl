<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<table class="table table-hover">
    <thead>
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Email</th>
        <th>Registration Date</th>
    </tr>
    </thead>
    <tbody>
            <#list teachersList as user>
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.registrationDate}</td>
                    <td><a href="edit-teachers/${user.id}">Edit</a></td>
                </tr>
            </#list>
    </tbody>
</table>

</body>
</html>