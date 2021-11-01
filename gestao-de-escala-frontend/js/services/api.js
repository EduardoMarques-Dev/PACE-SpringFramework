import { http } from './config.js'

export default { 
    listarAdvogados:() => {                   //advogados
        return http.get('advogados/')
    },
    salvarAdvogado:(advogado) => {
        return http.post('advogados/', advogado)
    },
    deletarAdvogado:(id) => {
        return http.delete('advogados/'+ id)
    },
    editarAdvogado:(id, advogado) => {
        return http.put('advogados/'+ id, advogado)
    },
    gerarEscala:() => {                   //escala
        return http.post('escala/')
    }                               
}