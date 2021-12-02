import Api from './api.js' 

var id = sessionStorage.getItem('idAdvogado');
var nome = sessionStorage.getItem('nomeAdvogado');
var numeroOAB = sessionStorage.getItem('numeroOAB');

var advogadoJson = {
    "nomeAdvogado": "",
    "numeroOAB": ""
}

if(numeroOAB == "null" || numeroOAB == "undefined" ){
    numeroOAB = "";
}
document.getElementById("nome_advogado_editar").value = nome;
document.getElementById("numero_OAB_editar").value = numeroOAB;


function editar(advogadoJson){
    Api.editarAdvogado(id, advogadoJson).then(response => {
        console.log("Status", response.status);
                
        //limparCampos();
        window.history.back();
    }).catch(error => console.error(error));
}

$('#salvar-editado').click( function () {
    nome = document.querySelector('#nome_advogado_editar').value.trim();
    numeroOAB = document.querySelector('#numero_OAB_editar').value.trim();
    if(nome == ""){
        console.log("Nome não pode ser nulo");
    }else{
        advogadoJson.nomeAdvogado = nome;
        advogadoJson.numeroOAB = numeroOAB;
        editar(advogadoJson); 
    }
});

function limparCampos(){
    document.getElementById("nome_advogado_editar").value = "";
    document.getElementById("numero_OAB_editar").value = "";
    
    var limpar = "";
    sessionStorage.setItem('idAdvogado', limpar);  
    sessionStorage.setItem('nomeAdvogado', limpar);
    sessionStorage.setItem('numeroOAB', limpar);
}