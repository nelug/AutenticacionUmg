

var app = new Vue({
    el: '#usuariosList',
    data: {
        listaUsuarios: [],
        userInfo: {
            nombre: "",
            apellido: "",
            nombre_completo: "",
            id: "",
            token: "",
            red: ""
        }
    },
    methods: {
        getInfo: function(user){
            app.userInfo.nombre = user.nombre.stringValue;
            app.userInfo.apellido = user.apellido.stringValue;
            app.userInfo.nombre_completo = user.nombre_completo.stringValue;
            app.userInfo.id = user.id.stringValue;
            app.userInfo.red = user.red.stringValue;
            app.userInfo.token_cloud_message = user.token_cloud_message.stringValue;
            $('#showInfo').modal();
            $('#showInfo').modal('open');
        }
    }

});

$.get( "https://firestore.googleapis.com/v1beta1/projects/appmoviles-89341/databases/(default)/documents/usuarios", 
function( data ) {
    app.listaUsuarios = data.documents; 
});

