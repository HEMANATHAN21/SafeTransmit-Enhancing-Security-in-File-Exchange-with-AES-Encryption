<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PDF Viewer</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            background: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)),
                url("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcReeryYIAE6zYYxUDvKznYa9dWVRdLx711GDw&usqp=CAU")
                center/cover no-repeat fixed;
            height: 100vh;
            font-family: 'Arial', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            position: relative;
            width: 45%;
            padding: 20px;
            border-radius: 20px;
            box-shadow: 0px 5px 25px rgba(0, 0, 0, 0.2);
            display: flex;
            justify-content: space-evenly;
            align-items: center;
            flex-direction: column;
            background-color: rgba(255, 255, 255, 0.9);
            color: #333;
            transition: transform 0.3s;
            opacity: 0;
            animation: fadeIn 1s ease-out forwards;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
        }

        .container:hover {
            transform: scale(1.02);
        }

        .container form {
            width: 30%; /* Adjust the width of the form */
            display: flex;
            justify-content: space-evenly;
            align-items: center;
            flex-direction: column;
        }

        .inputbox {
            width: 100%;
            margin: 10px 0;
            text-align: left;
        }

        .inputbox label {
            display: block;
            margin-bottom: 5px;
            color: #555;
        }

        .inputbox input[type="text"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 16px;
            color: #444;
        }

        .filebox {
            width: 100%;
            margin: 10px 0;
            text-align: left;
        }

        .filebox label {
            display: block;
            margin-bottom: 5px;
            color: #555;
        }

        .filebox input[type="file"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 16px;
            color: #444;
        }

        .inputbox input[type="submit"] {
            width: 100%;
            cursor: pointer;
            background-color: rgb(128, 255, 255);
            color: white;
            border: none;
            padding: 15px;
            border-radius: 5px;
            font-size: 18px;
            transition: background-color 0.3s;
        }

        .inputbox input[type="submit"]:hover {
            background-color: #45a049;
        }

        .cont {
            margin-top: 20px;
        }

        .payment-link {
            position: absolute;
            top: 10px;
            right: 10px;
            color: #fff;
            text-decoration: none;
            font-size: 16px;
        }

        .external-form {
            position: absolute;
            top: 50px;
            right: 20px;
            background-color: rgba(255, 255, 255, 0.8);
            padding: 15px;
            border-radius: 10px;
            box-shadow: 0px 5px 25px rgba(0, 0, 0, 0.2);
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .external-form input[type="submit"] {
            width: 100%;
            cursor: pointer;
            background-color: rgb(128, 255, 255);
            color: white;
            border: none;
            padding: 15px;
            border-radius: 5px;
            font-size: 16px;
            transition: background-color 0.3s;
        }

        .external-form input[type="submit"]:hover {
            background-color: #45a049;
        }

        .column {
            width: 70%; /* Adjust the width of the iframe */
        }

        #pdfFrame {
            width: 100%;
            height: 800px;
            oncontextmenu: "return false;";
        }

        #loading {
            display: none;
            justify-content: center;
            align-items: center;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(255, 255, 255, 0.8);
            z-index: 1000;
        }

        #loading img {
            width: 50px;
            height: 50px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 style="color: #fff;">Enter Secret Key to Fetch PDF</h2>
        <form onsubmit="fetchPdf(); return false;">
            <div class="inputbox">
                <label for="secretKey">Enter Customer Secret Key:</label>
                <input type="text" id="secretKey" placeholder="Enter Key" required>
            </div>
            <div class="inputbox">
                <input type="submit" id="fetchButton" value="Fetch PDF">
            </div>
        </form>
    </div>

    <div class="column">
        <iframe id="pdfFrame"></iframe>
    </div>

    <div id="loading">
        <img src="https://media0.giphy.com/media/Pn3nDa3ce9ywIpvnmn/giphy.webp?cid=ecf05e47b9ev3xabhib9v4rqf6htd5tzpsthfucp2zp8nmn6&ep=v1_gifs_search&rid=giphy.webp&ct=g" alt="Loading">
    </div>

    <script>
        // JavaScript function to fetch and display PDF
        function fetchPdf() {
            var secretKey = document.getElementById("secretKey").value;
            var loadingDiv = document.getElementById("loading");

            // Display loading animation
            loadingDiv.style.display = "flex";

            // Create a new XMLHttpRequest object
            var xhr = new XMLHttpRequest();

            // Configure it: GET-request for the given URL, with asynchronous mode
            xhr.open("GET", "seckeyinp?secretKey=" + secretKey, true);

            // Set the responseType to 'arraybuffer' to handle binary data
            xhr.responseType = 'arraybuffer';

            // Define the callback function to handle the response
            xhr.onload = function () {
                // Hide loading animation
                loadingDiv.style.display = "none";

                if (xhr.status === 200) {
                    // On success, update the iframe source with the PDF data and hide toolbar
                    var blob = new Blob([xhr.response], { type: 'application/pdf' });
                    var pdfUrl = URL.createObjectURL(blob);
                    document.getElementById("pdfFrame").src = pdfUrl + '#toolbar=0&navpanes=0';
                } else {
                    console.error('Error fetching PDF:', xhr.statusText);
                }
            };

            // Define the callback function for network errors
            xhr.onerror = function () {
                // Hide loading animation
                loadingDiv.style.display = "none";

                console.error('Network error while fetching PDF:', xhr.statusText);
            };

            // Send the request
            xhr.send();
        }
    </script>
</body>
</html>
