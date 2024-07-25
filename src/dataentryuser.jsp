<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Data Entry User - Project 3 Enterprise System</title>
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
        .form-section {
            margin-bottom: 20px;
        }
        input[type=text] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }
        input[type=button] {
            width: 100%;
            min-width: 280px; /* Adjusted to ensure the text fits */
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
        .section-title {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
    <script>
        function clearForm(formId) {
            document.getElementById(formId).reset();
            document.getElementById("results").innerHTML = "";
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>Welcome to the Summer 2024 Project 3 Enterprise System</h1>
        <h2>Data Entry Application</h2>
        <div class="form-container">
            <h3 class="section-title">Suppliers Record Insert</h3>
            <form id="suppliersForm">
                <div class="form-section">
                    <label for="snum">snum:</label>
                    <input type="text" id="snum" name="snum" required>
                </div>
                <div class="form-section">
                    <label for="sname">sname:</label>
                    <input type="text" id="sname" name="sname" required>
                </div>
                <div class="form-section">
                    <label for="status">status:</label>
                    <input type="text" id="status" name="status" required>
                </div>
                <div class="form-section">
                    <label for="city">city:</label>
                    <input type="text" id="city" name="city" required>
                </div>
                <input type="button" value="Enter Supplier Record into Database" onclick="submitForm('suppliersForm', 'suppliers')">
                <input type="button" value="Clear Results" onclick="clearForm('suppliersForm')">
            </form>
        </div>

        <div class="form-container">
            <h3 class="section-title">Parts Record Insert</h3>
            <form id="partsForm">
                <div class="form-section">
                    <label for="pnum">pnum:</label>
                    <input type="text" id="pnum" name="pnum" required>
                </div>
                <div class="form-section">
                    <label for="pname">pname:</label>
                    <input type="text" id="pname" name="pname" required>
                </div>
                <div class="form-section">
                    <label for="color">color:</label>
                    <input type="text" id="color" name="color" required>
                </div>
                <div class="form-section">
                    <label for="weight">weight:</label>
                    <input type="text" id="weight" name="weight" required>
                </div>
                <div class="form-section">
                    <label for="city">city:</label>
                    <input type="text" id="city" name="city" required>
                </div>
                <input type="button" value="Enter Parts Record into Database" onclick="submitForm('partsForm', 'parts')">
                <input type="button" value="Clear Results" onclick="clearForm('partsForm')">
            </form>
        </div>

        <div class="form-container">
            <h3 class="section-title">Jobs Record Insert</h3>
            <form id="jobsForm">
                <div class="form-section">
                    <label for="jnum">jnum:</label>
                    <input type="text" id="jnum" name="jnum" required>
                </div>
                <div class="form-section">
                    <label for="jname">jname:</label>
                    <input type="text" id="jname" name="jname" required>
                </div>
                <div class="form-section">
                    <label for="numworkers">numworkers:</label>
                    <input type="text" id="numworkers" name="numworkers" required>
                </div>
                <div class="form-section">
                    <label for="city">city:</label>
                    <input type="text" id="city" name="city" required>
                </div>
                <input type="button" value="Enter Job Record into Database" onclick="submitForm('jobsForm', 'jobs')">
                <input type="button" value="Clear Results" onclick="clearForm('jobsForm')">
            </form>
        </div>

        <div class="form-container">
            <h3 class="section-title">Shipments Record Insert</h3>
            <form id="shipmentsForm">
                <div class="form-section">
                    <label for="snum">snum:</label>
                    <input type="text" id="snum" name="snum" required>
                </div>
                <div class="form-section">
                    <label for="pnum">pnum:</label>
                    <input type="text" id="pnum" name="pnum" required>
                </div>
                <div class="form-section">
                    <label for="jnum">jnum:</label>
                    <input type="text" id="jnum" name="jnum" required>
                </div>
                <div class="form-section">
                    <label for="quantity">quantity:</label>
                    <input type="text" id="quantity" name="quantity" required>
                </div>
                <input type="button" value="Enter Shipment Record into Database" onclick="submitForm('shipmentsForm', 'shipments')">
                <input type="button" value="Clear Results" onclick="clearForm('shipmentsForm')">
            </form>
        </div>

        <div id="results" class="results-container">
            <!-- Results will be displayed here -->
        </div>
    </div>

    <script>
        function submitForm(formId, table) {
            const form = document.getElementById(formId);
            const formData = new FormData(form);
            const params = new URLSearchParams(formData);

            fetch(`dataentryuser?table=${table}`, {
                method: 'POST',
                body: params
            })
            .then(response => response.text())
            .then(data => {
                document.getElementById("results").innerHTML = data;
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }
    </script>
</body>
</html>
