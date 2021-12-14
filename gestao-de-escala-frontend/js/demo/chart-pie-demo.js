import baseURL from '../../ambiente/baseURL.js'

var url = baseURL + 'mutiroes/';

var backgroundColor = [ 'darkBlue','#4e73df', '#36b9cc', '#037665', '#0e8e7b', '#1cc88a','green', '#e83e8c', 'purple', '#6f42c1', 'red', 'orange', 'yellow', 'pink', 'black', 'grey'];//16

var listaVaras = [];
var listaNomeVaras = [];
var listaCores = [];
var listaTotalDePautas = [];

//Atualizar varas com mutirão
await listarVaras();
iniciarPie();

async function listarVaras(){
  await axios.get(url).then(response => {
    var mutiroes = response.data;
    muti(mutiroes);

    var indice = 0;
    listaNomeVaras.forEach(function(nomeVara){
    
    var varaJson = { "nome": nomeVara, "pautas": 0, "cor" : backgroundColor[indice] }

    var mutirao = mutiroes.filter(item =>  item.vara == nomeVara);
    mutirao.forEach(function(muti){
      varaJson.pautas = varaJson.pautas + muti.quantidaDePautas;
    });
    listaVaras.push(varaJson);
    listaCores.push(varaJson.cor);
    listaTotalDePautas.push(varaJson.pautas);
    indice ++;
  });

  }).catch(error => console.error(error));
}

async function muti(mutiroes){
  var option = '';
  
  var naoTem =  true;
  listaNomeVaras[0] = mutiroes[0].vara;

  $.each(mutiroes, function(i, obj){
    // console.log(i)
    if(i == 0){
      option += '<option value="'+obj.vara+'">'+obj.vara+'</option>';
    }
    listaNomeVaras.forEach(function(vara){
      if(obj.vara == vara){
        naoTem = false;
      }else{
        naoTem = true;
      }
    });
    if(naoTem){
      listaNomeVaras.push(obj.vara);
      option += '<option value="'+obj.vara+'">'+obj.vara+'</option>';
    }
  }) 
  // $('#vara').html(option).show(); 
}

async function iniciarPie(){
   var labelsVaras = '';

  $.each(listaVaras, function(i, vara){
    labelsVaras += 
      '<span class="mr-2">'+
          '<i class="fas fa-circle" style="color: '+vara.cor+'" ></i>'+ vara.nome+
      '</span>';
  });
  $('#labelsVaras').html(labelsVaras).show();  
}

// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

// Pie Chart Example
var ctx = document.getElementById("myPieChart");
var myPieChart = new Chart(ctx, {
  type: 'doughnut',
  data: {
    labels: listaNomeVaras,
    datasets: [{
      data: listaTotalDePautas,
      backgroundColor: backgroundColor,
      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf'],
      hoverBorderColor: "rgba(234, 236, 244, 1)",
    }],
  },
  options: {
    maintainAspectRatio: false,
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
    },
    legend: {
      display: false
    },
    cutoutPercentage: 80,
  },
});