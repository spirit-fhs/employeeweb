$(function() {
  $( "#expires_id_field" ).datepicker($.datepicker.regional['de']);
});

// increase the default animation speed to exaggerate the effect
$.fx.speeds._default = 1000;
$(function() {
$( "#dialog" ).dialog({
                    height: 350,
                    width: 650,
                    autoOpen: false,
                    show: "slide",
                    hide: "slide",
                    position: 'top',
                    buttons: [
                    {
                        text: "Zurück",
                        click: function() { $(this).dialog("close"); }
                    },
                    {
                        text: "Senden",
                        click: function() {
                         $("form:first").submit();
                        }
                    }
                    ],
                    modal: true

                });

$( "#onclick" ).click(function() {
    $( "#dialog" ).dialog( "open" );
        return false;
    });
});
