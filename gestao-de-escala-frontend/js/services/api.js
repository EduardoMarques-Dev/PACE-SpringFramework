import { http } from './config.js'

export default { 
    listarAdvogados:() => {                   //advogados
        return http.get('advogado/')
    },
    salvarAdvogado:(advogado) => {
        return http.post('advogado/', advogado)
    },
    deletarAdvogado:(id) => {
        return http.delete('advogado/'+ id)
    },
    editarAdvogado:(id, advogado) => {
        return http.put('advogado/'+ id, advogado)
    },
    gerarEscala:() => {                   //escala
        return http.post('escala/')
    }                               
}