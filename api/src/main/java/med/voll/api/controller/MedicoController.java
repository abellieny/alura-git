package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados)    {
        //System.out.println(dados);

        repository.save(new Medico(dados));

    }

/* sem paginação
    @GetMapping
    public List<DadosListagemMedico> listar(Pageable paginacao){
        return repository.findAll(paginacao).stream().map(DadosListagemMedico::new).toList();
    }
*/
    /*com paginação
    ?size=1&page=2
    ?sort=atributo do json, desc(ordem descendente)
     */
    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"} )Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemMedico::new);
    }

    @GetMapping("/listarAtivo")
    public Page<DadosListagemMedico> listarAtivo(@PageableDefault(size = 10, sort = {"nome"} )Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
//testando o gti
    }

/*    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id) {
        repository.deleteById(id);
    }
    */
    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.excluir();
    }
}
