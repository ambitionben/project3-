<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Client User - Project 3 Enterprise System</title>
    <style>
        body {
            background-color: black;
            color: lime;
            text-align: center;
            font-family: Arial, sans-serif;
        }
        .container {
            margin-top: 50px;
        }
        .form-container {
            display: inline-block;
            text-align: left;
            background-color: #222;
            padding: 20px;
            border-radius: 10px;
        }
        textarea {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        input[type=submit], input[type=button] {
            width: 100%;
            background-color: green;
            color: white;
            padding: 10px;
            margin: 10px 0;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type=submit]:hover, input[type=button]:hover {
            background-color: darkgreen;
        }
        .results-container {
            background-color: white;
            color: black;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
        }
        .error-box {
            color: red;
            border: 2px solid red;
            padding: 10px;
            margin-top: 20px;
            border-radius: 10px;
            background-color: #ffcccc;
        }
    </style>
    <script>
        function executeCommand() {
            const sqlCommand = document.getElementById("sqlCommand").value;
            console.log("Sending AJAX request with command:", sqlCommand);
            const xhr = new XMLHttpRequest();
            xhr.open("POST", "ExecuteSQLServletClient", true);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4) {
                    console.log("AJAX request completed with status:", xhr.status);
                    if (xhr.status == 200) {
                        console.log("Response received:", xhr.responseText);
                        if (xhr.responseText.startsWith("ERROR:")) {
                            const errorMsg = xhr.responseText.split("\n")[0].replace("ERROR:", "");
                            const sqlCmd = xhr.responseText.split("\n")[1].replace("COMMAND:", "");
                            document.getElementById("results").innerHTML = "<div class='error-box'><p>Error executing SQL command:</p><p><b>" + errorMsg + "</b></p><p><b>Command:</b> " + sqlCmd + "</p></div>";
                        } else {
                            document.getElementById("results").innerHTML = xhr.responseText;
                        }
                    } else {
                        console.error("Error response received:", xhr.responseText);
                    }
                }
            };

            xhr.send("sqlCommand=" + encodeURIComponent(sqlCommand));
        }

        function resetForm() {
            document.getElementById("sqlCommand").value = "";
        }

        function clearResults() {
            document.getElementById("results").innerHTML = "";
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>Welcome, Client User</h1>
        <h2>Project 3 Enterprise System</h2>
        <div class="form-container">
            <label for="sqlCommand">Enter SQL Command:</label>
            <textarea id="sqlCommand" name="sqlCommand" rows="4" required></textarea>
            <input type="button" value="Execute Command" onclick="executeCommand()">
            <input type="button" value="Reset Form" onclick="resetForm()">
            <input type="button" value="Clear Results" onclick="clearResults()">
        </div>
        <div id="results" class="results-container">
            <!-- Results will be displayed here -->
        </div>
    </div>
</body>
</html>
