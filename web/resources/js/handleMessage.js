/*Este Script funciona para 10 Botones
 *para agregar uno nuevo se debe agregar el Método handleMessage correspondiente*/

//BOTON 1
function handleMessage1(data) {

    var status = data.status;

    if (status == 1) {
        $('.display11').html(function() {
            $('.display11').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display12').html(function() {
            $('.display11').hide();
            $('.display12').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display13').html(function() {
            $('.display12').hide();
            $('.display13').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display11').hide();
        $('.display12').hide();
        $('.display13').hide();
    }

}


//BOTON 2
function handleMessage2(data) {

    var status = data.status;

    if (status == 1) {
        $('.display21').html(function() {
            $('.display21').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display22').html(function() {
            $('.display21').hide();
            $('.display22').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display23').html(function() {
            $('.display22').hide();
            $('.display23').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display21').hide();
        $('.display22').hide();
        $('.display23').hide();
    }

}

//BOTON 3
function handleMessage3(data) {

    var status = data.status;
    
    if (status == 1) {
        //alert("Entro al Script Status 1");
        $('.display31').html(function() {
            $('.display31').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
       // alert("Entro al Script Status 2");
        $('.display32').html(function() {
            $('.display31').hide();
            $('.display32').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        //alert("Entro al Script Status 3");
        $('.display33').html(function() {
            $('.display32').hide();
            $('.display33').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display31').hide();
        $('.display32').hide();
        $('.display33').hide();
    }

}

//BOTON 4
function handleMessage4(data) {

    var status = data.status;

    if (status == 1) {
        $('.display41').html(function() {
            $('.display41').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display42').html(function() {
            $('.display41').hide();
            $('.display42').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display43').html(function() {
            $('.display42').hide();
            $('.display43').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display41').hide();
        $('.display42').hide();
        $('.display43').hide();
    }

}

//BOTON 5
function handleMessage5(data) {

    var status = data.status;

    if (status == 1) {
        $('.display51').html(function() {
            $('.display51').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display52').html(function() {
            $('.display51').hide();
            $('.display52').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display53').html(function() {
            $('.display52').hide();
            $('.display53').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display51').hide();
        $('.display52').hide();
        $('.display53').hide();
    }

}

//BOTON 6
function handleMessage6(data) {

    var status = data.status;

    if (status == 1) {
        $('.display61').html(function() {
            $('.display61').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display62').html(function() {
            $('.display61').hide();
            $('.display62').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display63').html(function() {
            $('.display62').hide();
            $('.display63').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display61').hide();
        $('.display62').hide();
        $('.display63').hide();
    }

}

//BOTON 7
function handleMessage7(data) {

    var status = data.status;

    if (status == 1) {
        $('.display71').html(function() {
            $('.display71').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display72').html(function() {
            $('.display71').hide();
            $('.display72').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display73').html(function() {
            $('.display72').hide();
            $('.display73').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display71').hide();
        $('.display72').hide();
        $('.display73').hide();
    }

}

//BOTON 8
function handleMessage8(data) {

    var status = data.status;

    if (status == 1) {
        $('.display81').html(function() {
            $('.display81').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display82').html(function() {
            $('.display81').hide();
            $('.display82').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display83').html(function() {
            $('.display82').hide();
            $('.display83').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display81').hide();
        $('.display82').hide();
        $('.display83').hide();
    }

}

//BOTON 9
function handleMessage9(data) {

    var status = data.status;

    if (status == 1) {
        $('.display91').html(function() {
            $('.display91').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display92').html(function() {
            $('.display91').hide();
            $('.display92').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display93').html(function() {
            $('.display92').hide();
            $('.display93').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display91').hide();
        $('.display92').hide();
        $('.display93').hide();
    }

}

//BOTON 10
function handleMessage10(data) {

    var status = data.status;

    if (status == 1) {
        $('.display101').html(function() {
            $('.display101').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 2) {
        $('.display102').html(function() {
            $('.display101').hide();
            $('.display102').show();
            return '<p><h>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 3) {
        $('.display103').html(function() {
            $('.display102').hide();
            $('.display103').show();
            return '<p><h2>Ubicación: ' + data.ubicacion + '</h2></p><p>Tiempo: ' + data.counter + '</p>';
        });
    }

    if (status == 4) {
        $('.display101').hide();
        $('.display102').hide();
        $('.display103').hide();
    }

}

