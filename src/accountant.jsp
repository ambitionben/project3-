<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Accountant User - Project 3 Enterprise System</title>
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
            margin-bottom: 30px;
        }
        input[type=radio] {
            margin: 10px;
        }
        input[type=button] {
            width: 100%;
            min-width: 280px;
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
    </style>
    <script>
        function executeReport() {
            const selectedReport = document.querySelector('input[name="report"]:checked').value;
            fetch(`accountant?report=${selectedReport}`, {
                method: 'GET',
            })
            .then(response => response.text())
            .then(data => {
                document.getElementById("results").innerHTML = data;
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }

        function clearResults() {
            document.getElementById("results").innerHTML = "";
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>Welcome to the Summer 2024 Project 3 Enterprise System</h1>
        <h2>Accountant Application</h2>
        <div class="form-container">
            <h3>Select a report to generate:</h3>
            <input type="radio" id="report1" name="report" value="Get_Maximum_Status" checked>
            <label for="report1">Get the Maximum Status Value of All Suppliers</label><br>
            <input type="radio" id="report2" name="report" value="Get_Total_Weight_Parts">
            <label for="report2">Get the Total Weight of All Parts</label><br>
            <input type="radio" id="report3" name="report" value="Get_Total_Number_Shipments">
            <label for="report3">Get the Total Number of Shipments</label><br>
            <input type="radio" id="report4" name="report" value="Get_Name_NumWorkers_MostWorkers_Job">
            <label for="report4">Get the Name and Number of Workers of the Job with the Most Workers</label><br>
            <input type="radio" id="report5" name="report" value="List_Name_Status_Suppliers">
            <label for="report5">List the Name and Status of Every Supplier</label><br>
            <input type="button" value="Execute Command" onclick="executeReport()">
            <input type="button" value="Clear Results" onclick="clearResults()">
        </div>
        <div id="results" class="results-container">
            <!-- Results will be displayed here -->
        </div>
    </div>
</body>
</html>
