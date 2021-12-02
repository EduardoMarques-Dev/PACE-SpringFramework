import baseURL from '../../ambiente/baseURL.js'

var url = baseURL + 'advogados/'; 

var advogado;
var advogados;
var mensagem = '';

var advogadoJson = {
  "nomeAdvogado": "",
  "numeroOAB": ""
}

///////////EXIBIÇÃO ///////////

window.onload = function() {
  var id = "";
  sessionStorage.setItem('idAdvogado', id);

  axios.get(url).then(response => {
    advogados = response.data;
    advogados.forEach(listar);
  }).catch(error => console.error(error));

};

function listar(advogado){
  var tabela = $('#dataTable').DataTable();
  tabela.row.add( [
    advogado.nomeAdvogado,
    advogado.numeroOAB
  ] ).draw( false );
}

function cadastrar(advogado){
  axios.post(url, advogado).then(response => {
    console.log(response.status);
    if(response.status == 200){
      alertar('Advogado foi cadastrado.');
    }
    listar(advogado);
    document.getElementById("nome_advogado").value = "";
    document.getElementById("numero_OAB").value = "";
  }).catch(error => console.error(error));
}

function deletar(id){
  axios.delete(url + id).then(response => {
    console.log(response.status);
  }).catch(error => console.error(error));
}

function pesquisar(advogadoJson){
  var tabela = $('#dataTable').DataTable();
  var advogadoDaPesquisa; 
  axios.get(url).then(response => {
    advogados = response.data;
    advogadoDaPesquisa = advogados;

    if(advogadoJson.nomeAdvogado){
      advogadoDaPesquisa = advogados.filter(item =>  item.nomeAdvogado == advogadoJson.nomeAdvogado);
    }
    if(advogadoDaPesquisa && advogadoJson.numeroOAB){
      advogadoDaPesquisa = advogadoDaPesquisa.filter(item =>  item.numeroOAB == advogadoJson.numeroOAB);
    }
   
    //advogadoDaPesquisa = advogadoDaPesquisa[0];
    if(advogadoDaPesquisa){
      
      tabela.rows().remove().draw(); 
    
      advogadoDaPesquisa.forEach(listar);
    }else{
      tabela.rows().remove().draw();
      advogados.forEach(listar);
    }
    console.log("Status "+response.status);
      
  }).catch(error => console.error(error)); 
}


//////////// BOTÕES /////////////

  $('#cadastrarAdvogado').on( 'click', function () {
    var nome = document.querySelector('#nome_advogado').value.trim();
    var numeroOAB = document.querySelector('#numero_OAB').value.trim();
    numeroOAB = numeroOAB.replace(/\D/g,"");

    var nome0 = document.querySelector('#nome_advogado').value.trim();
    var nome = nome0.toUpperCase();

    axios.get(url).then(response => {
      advogados = response.data;
      if(nome != ""){
        advogadoJson.nomeAdvogado = nome;
        advogadoJson.numeroOAB = numeroOAB;
        var existe = advogados.filter(item =>  item.nomeAdvogado == nome);
        if(existe[0])
          alert("Nome já cadastrado");
        else
          cadastrar(advogadoJson);
      }
    }).catch(error => console.error(error));

  });

  function alertar(aviso){
    alert(aviso);
  }
  
  function campoNulo(campo){
    alert('Aviso: ' + campo + ' é obrigatório.');
  }

  $("#nome_advogado").on("input", function(){
    var regexp = /[^a-zA-Z-áàâãéèêíïóôõöúçÁÀÂÃÉÈÍÏÓÔÕÖÚÇ`´ ]/g;
    if(this.value.match(regexp)){
      $(this).val(this.value.replace(regexp,''));
    }
  });

$('#excluir').click( function () {
  var tabela = $('#dataTable').DataTable();
  var advogado = tabela.row('.selected').data();
  advogadoJson = advogados.filter(item =>  item.nomeAdvogado == advogado[0]);
  advogadoJson = advogadoJson[0];
  var id = advogadoJson.id;
  deletar(id);  
  tabela.row('.selected').remove().draw( false );
} );

function delayRefreshPage(mileSeconds) {
  window.setTimeout(pesquisar,mileSeconds);
}

$('#editar').click( function () {
  var tabela = $('#dataTable').DataTable();
  var advogado = tabela.row('.selected').data();
  advogadoJson = advogados.filter(item =>  item.nomeAdvogado == advogado[0]);
  advogadoJson = advogadoJson[0];

  var id = advogadoJson.id; 
  var nomeAdvogado = advogadoJson.nomeAdvogado; 
  var numeroOAB = advogadoJson.numeroOAB; 
  
  sessionStorage.setItem('idAdvogado', id);
  sessionStorage.setItem('nomeAdvogado', nomeAdvogado);
  sessionStorage.setItem('numeroOAB', numeroOAB);
});

$('#pesquisar').click( function () { 
  var inputNome = document.getElementById("nome_advogado_pesquisa").value.trim();
  advogadoJson.nomeAdvogado = inputNome.toUpperCase()

  var inputNumeroOAB = document.getElementById("numero_OAB_pesquisa").value.trim();
  
  advogadoJson.numeroOAB = inputNumeroOAB.replace(/\D/g,"");
  //location.reload();

  pesquisar(advogadoJson);
});