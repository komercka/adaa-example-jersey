<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

    <title>ADAA API client reference example</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
            integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet"
          integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">

</head>
<body>
<nav class="navbar navbar-dark bg-primary justify-content-between">
    <span class="navbar-brand"><b>DEMO aplikace</b></span>
    <div class="text-light"><span>František Koudelka</span> <i class="fa fa-user-circle-o fa-lg" aria-hidden="true"></i>
    </div>
</nav>
<div class="container w-50">
    <div class="justify-content-center mt-4 p-3 text-center border">
            <div>
                <h3>Authorizace klienta</h3>
            </div>
            <p class="text-left">
                Pro zobrazení vašich trasnakcí z účtů KB povolte službu Přímý přístup k účtu prostřednictvím API
            </p>
            <button id="registerBtn" class="btn btn-primary">Povolit službu</button>
    </div>
</div>
<script type="text/javascript">
    document.getElementById("registerBtn").onclick = function () {
        window.location.replace(window.location.origin + "/register/software");
    };
</script>
</body>
</html>