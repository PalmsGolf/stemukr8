<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admins</title>
</head>
<body>
<table class="table table-hover">
    <thead>
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Email</th>
    </tr>
    </thead>
    <tbody>
     <#list admin_list as user>
     <tr>
         <td>${user.id}</td>
         <td>${user.username}</td>
         <td>${user.email}</td>
         <td>${user.registrationDate}</td>
     </tr>
     </#list>
    </tbody>
</table>
</body>
</html>