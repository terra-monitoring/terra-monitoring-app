<?php
    $servername = "http://pi-terra.ddns.de/db/";
    $username = "pi";
    $password = "terra5#";
    $dbname = "terra.sqlite";
    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }
    catch(PDOException $e) {
        die("OOPs something went wrong");
    }
?>