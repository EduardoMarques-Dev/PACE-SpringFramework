import baseURL from '../../ambiente/baseURL.js'

var url = baseURL + 'procuradores/';
var pautistas;
var nomeProcurador;

var pautistaJson = {
"id":"",
"nomeProcurador": "",
"status": "",
"dataInicial":"",
"dataFinal":"",
"grupo":"",
"saldo":"0",
"peso": ""
}

window.onload = function() {
  if(sessionStorage.getItem('nomeProcurador') == 'false'){
    window.history.back();
  }
    //Function getprocuradores(){...
    axios.get(url).then(response => {
        pautistas = response.data;
        nomeProcurador = sessionStorage.getItem('nomeProcurador');
        pautistaJson = pautistas.filter(item =>  item.nomeProcurador == nomeProcurador);
        pautistaJson = pautistaJson[0];
        document.getElementById("nome-pautista").value = pautistaJson.nomeProcurador;
        document.getElementById("status").value = pautistaJson.status;
        document.getElementById("data-inicial").value = pautistaJson.dataInicial;
        document.getElementById("data-final").value = pautistaJson.dataFinal;
        document.getElementById("grupo").value = pautistaJson.grupo;
        document.getElementById("peso").value = pautistaJson.peso;
 
    }).catch(error => console.error(error)); 
};

$('#salvar').on( 'click', function () {
  
    pautistaJson.nomeProcurador= document.querySelector('#nome-pautista').value;
    pautistaJson.status = document.querySelector('#status').value;
    pautistaJson.dataInicial = document.querySelector('#data-inicial').value;
    pautistaJson.dataFinal = document.querySelector('#data-final').value;
    pautistaJson.grupo = document.querySelector('#grupo').value;
    pautistaJson.peso = document.querySelector('#peso').value;
   
    if(pautistaJson.nomeProcurador != ""){
      editar(pautistaJson);
    } 
  });

  function editar(pautistaJson){
    axios.put(url + pautistaJson.id, pautistaJson).then(response => {
      //console.log(response.status);
      //limparCampos();
      window.history.back();
    }).catch(error => console.error(error));
}

  
// var nome = document.querySelector('#nome_pautista').value;
// advogado.nome = nome;
// axios.post(url,
// {nomeProcurador: nome}
// ).then(response =>{
// listar(response.data.nomeProcurador);
// }).catch(error => console.error(error));