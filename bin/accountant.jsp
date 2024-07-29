<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Accountant Reports - Project 3 Enterprise System</title>
    <style>
        body {
            background-color: #222;
            color: #ccc;
            text-align: center;
            font-family: Arial, sans-serif;
        }
        .container {
            margin-top: 50px;
        }
        .form-container {
            display: inline-block;
            text-align: left;
            background-color: #333;
            padding: 20px;
            border-radius: 10px;
        }
        select {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        input[type=button] {
            width: 100%;
            background-color: green;
            color: white;
            padding: 10px;
            margin: 10px 0;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type=button]:hover {
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
        function executeReport() {
            const cmd = document.getElementById("cmd").value;
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "accountant?cmd=" + cmd, true);

            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4) {
                    if (xhr.status == 200) {
                        document.getElementById("results").innerHTML = xhr.responseText;
                    } else {
                        document.getElementById("results").innerHTML = "<div class='error-box'>Error: " + xhr.statusText + "</div>";
                    }
                }
            };

            xhr.send();
        }

        function clearResults() {
            document.getElementById("results").innerHTML = "";
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>Accountant Reports</h1>
        <h2>Project 3 Enterprise System</h2>
        <div class="form-container">
            <label for="cmd">Select Report:</label>
            <select id="cmd" name="cmd">
                <option value="1">Sum of All Part Weights</option>
                <option value="2">Maximum Status of All Suppliers</option>
                <option value="3">Total Number of Shipments</option>
                <option value="4">Job with the Most Workers</option>
                <option value="5">Name and Status of All Suppliers</option>
            </select>
            <input type="button" value="Execute Report" onclick="executeReport()">
            <input type="button" value="Clear Results" onclick="clearResults()">
        </div>
        <div id="results" class="results-container">
            <!-- Results will be displayed here -->
        </div>
    </div>
</body>
</html>
