import baseURL from '../../ambiente/baseURL.js'

var url = baseURL + 'procuradores/';
var procuradores;
var mensagem = '';

var pautistaJson = {
  "nomeProcurador": "",
  "status": "",
  "dataInicial":"",
  "dataFinal":"",
  "grupo":"",
  "saldo": "",
  "peso": ""
}
///////////EXIBIÇÃO ///////////

window.onload = function() {
  //Function getprocuradores(){...
  axios.get(url).then(response => {
    procuradores = response.data;
    procuradores.forEach(listar);
  }).catch(error => console.error(error)); 
  sessionStorage.setItem('nomeProcurador', 'false');
};

function listar(procuradores){

  procuradores.dataInicial = formatarData(procuradores.dataInicial);
  procuradores.dataFinal = formatarData(procuradores.dataFinal);

  var tabela = $('#dataTable').DataTable();
    tabela.row.add( [
      procuradores.nomeProcurador,
      procuradores.grupo,
      procuradores.status,
      procuradores.dataInicial,
      procuradores.dataFinal,
      procuradores.saldo,
      procuradores.peso
    ] ).draw( false );
}

//////////// CRUD /////////////
 
function cadastrar(pautistaJson){
  
  console.log(pautistaJson)
  axios.post(url,pautistaJson).then(response =>{
    //console.log("response", response);
    if(response.status == 200){
      alertar('Pautista foi cadastrado.');
    }
    listar(pautistaJson);
    limparCampos();  
  }).catch(error => console.error(error));
}

function deletar(id){
  var table = $('#dataTable').DataTable();
  axios.delete(url+id).then(response => {
    console.log("response: " + response);
    table.row('.selected').remove().draw( false );
  }).catch(error => console.error(error));
}

////////////UTIL/////////////
function pesquisar(pautistaJson){
  var tabela = $('#dataTable').DataTable();
  var pautistaDaPesquisa; 
  axios.get(url).then(response => {
    var pautistas = response.data;
    pautistaDaPesquisa = pautistas;

    if(pautistaJson.nomeProcurador){
      pautistaDaPesquisa = pautistas.filter(item =>  item.nomeProcurador == pautistaJson.nomeProcurador);
    }
    if(pautistaDaPesquisa && pautistaJson.status){ 
      pautistaDaPesquisa = pautistaDaPesquisa.filter(item =>  item.status == pautistaJson.status);
    }
    if(pautistaDaPesquisa && pautistaJson.grupo){
      
    console.log(pautistaJson.nomeProcurador);
      pautistaDaPesquisa = pautistaDaPesquisa.filter(item =>  item.grupo == pautistaJson.grupo);
      
      
    console.log(pautistaDaPesquisa);
    }
   
    //pautistaDaPesquisa = pautistaDaPesquisa[0];
    if(pautistaDaPesquisa){
      
      tabela.rows().remove().draw(); 
    
      pautistaDaPesquisa.forEach(listar);
    }else{
      tabela.rows().remove().draw();
      pautistas.forEach(listar);
    }
    console.log("Status "+response.status);
      
  }).catch(error => console.error(error)); 
}

function formatarData(LocalDate){
  if(LocalDate !== null){
    if(LocalDate.indexOf("-") == 4 && LocalDate.length == 10){
      LocalDate= LocalDate.substring(8, 10)+"-"+LocalDate.substring(5, 7)+"-"+LocalDate.substring(0, 4);
    }
  }
  
  return LocalDate;
}

function limparCampos(){
  document.getElementById("nome-pautista").value = "";
  document.getElementById("status").value = "";
  // document.getElementById("data-inicial").value = "";
  // document.getElementById("data-final").value = "";
  // document.getElementById("status").value = "Ativo";
  document.getElementById("grupo").value = "Preposto";
  document.getElementById("peso").value = "1";
}
//////////// BOTÕES /////////////

$('#cadastrar-pautista').on( 'click', function () {
  
  pautistaJson.nomeProcurador= document.querySelector('#nome-pautista').value;
  // pautistaJson.status = document.querySelector('#status').value;
  // pautistaJson.dataInicial = document.querySelector('#data-inicial').value;
  // pautistaJson.dataFinal = document.querySelector('#data-final').value;
  pautistaJson.grupo = document.querySelector('#grupo').value.toUpperCase();
  pautistaJson.peso = document.querySelector('#peso').value;
  pautistaJson.status = "ATIVO";
 
  if(pautistaJson.nomeProcurador != ""){
    cadastrar(pautistaJson);
  } 
});

function alertar(aviso){
  alert(aviso);
}

function campoNulo(campo){
  alert('Aviso: ' + campo + ' é obrigatório.');
}

$('#editar').on( 'click', function () {
  var table = $('#dataTable').DataTable();
  var pautista = table.row('.selected').data();  
  var nomeProcurador = pautista[0];
  sessionStorage.setItem('nomeProcurador', nomeProcurador);
});

$('#excluir').on('click', function () {

  var table = $('#dataTable').DataTable();
  var pautista = table.row('.selected').data();
  var nomeProcurador = pautista[0];  
  //console.log(pautista[0])
  pautistaJson = procuradores.filter(item =>  item.nomeProcurador == nomeProcurador);
  var id = pautistaJson[0].id;// primeira pauta com o processo pesquisado
  //console.log(id);
  deletar(id);
});

$('#pesquisar').click( function () { 
  var inputNome = document.getElementById("nome-pautista").value.trim();
  pautistaJson.nomeProcurador = inputNome.toUpperCase()

  // pautistaJson.status = document.querySelector('#status').value.trim();
  //pautistaJson.dataInicial = document.querySelector('#data-inicial').value.trim();
  //pautistaJson.dataFinal = document.querySelector('#data-final').value.trim();
  pautistaJson.grupo = document.querySelector('#grupo').value.trim();
  //location.reload();

  pesquisar(pautistaJson);
});