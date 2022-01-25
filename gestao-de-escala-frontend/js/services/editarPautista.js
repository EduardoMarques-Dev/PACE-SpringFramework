import baseURL from '../../ambiente/baseURL.js'

var url = baseURL + 'pautista/';
var pautistas;
var nome;

var pautistaJson = {
"id":"",
"nome": "",
"status": "",
"dataInicial":"",
"dataFinal":"",
"grupo":"",
"saldo":"0",
"peso": ""
}

window.onload = function() {
  if(sessionStorage.getItem('nome') == 'false'){
    window.history.back();
  }
    //Function getprocuradores(){...
    axios.get(url).then(response => {
        pautistas = response.data;
        nome = sessionStorage.getItem('nome');
        pautistaJson = pautistas.filter(item =>  item.nome == nome);
        pautistaJson = pautistaJson[0];
        document.getElementById("nome-pautista").value = pautistaJson.nome;
        document.getElementById("status").value = pautistaJson.status.toUpperCase();
        document.getElementById("data-inicial").value = pautistaJson.dataInicial;
        document.getElementById("data-final").value = pautistaJson.dataFinal;
        document.getElementById("grupo").value = pautistaJson.grupo;
        document.getElementById("peso").value = pautistaJson.peso;
 
    }).catch(error => console.error(error)); 
};

$('#salvar').on( 'click', function () {
  
    pautistaJson.nome= document.querySelector('#nome-pautista').value;
    pautistaJson.status = document.querySelector('#status').value.toUpperCase();
    pautistaJson.dataInicial = document.querySelector('#data-inicial').value;
    pautistaJson.dataFinal = document.querySelector('#data-final').value;
    pautistaJson.grupo = document.querySelector('#grupo').value.toUpperCase();
    pautistaJson.peso = document.querySelector('#peso').value;
   
    if(pautistaJson.nome != ""){
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
// {nome: nome}
// ).then(response =>{
// listar(response.data.nome);
// }).catch(error => console.error(error));