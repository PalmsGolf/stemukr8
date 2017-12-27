<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
   <h1>Welcome ${admin.username}</h1>
   <a href="/admin/edit-teachers">View teachers' information</a>
   <a href="/admin/students">View students' information</a>
   <a href="/admin/admins">View admins' information</a>
   <a href="/admin/new_user">Add new user</a>
   <a href="/admin/edit-teachers/${admin.id}">Edit your own information</a>
</body>
</html>