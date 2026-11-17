package com.senaictti.chamados.Service;

import com.senaictti.chamados.Entity.grupo;
import com.senaictti.chamados.Entity.prioridade;
import com.senaictti.chamados.Entity.tipogrupo;
import com.senaictti.chamados.Repository.gruporepository;
import com.senaictti.chamados.Repository.prioridaderepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/*
 Garante que existam os grupos e prioridades básicos (só insere se a tabela estiver vazia).
 Sem isso não dá para abrir chamado, porque ele precisa de um grupo e de uma prioridade.
 */
@Component
public class catalogoseeder implements CommandLineRunner {

    private final gruporepository grupoRepository;
    private final prioridaderepository prioridadeRepository;

    public catalogoseeder(gruporepository grupoRepository, prioridaderepository prioridadeRepository) {
        this.grupoRepository = grupoRepository;
        this.prioridadeRepository = prioridadeRepository;
    }

    @Override
    public void run(String... args) {
        if (grupoRepository.count() == 0) {
            criarGrupo("N1", tipogrupo.PRODUCAO);
            criarGrupo("N2", tipogrupo.PRODUCAO);
            criarGrupo("N3", tipogrupo.PRODUCAO);
            criarGrupo("ADM", tipogrupo.ADMINISTRATIVO);
        }
        if (prioridadeRepository.count() == 0) {
            criarPrioridade("BAIXA", 72);
            criarPrioridade("MEDIA", 24);
            criarPrioridade("ALTA", 8);
            criarPrioridade("CRITICA", 4);
        }
    }

    private void criarGrupo(String nome, tipogrupo tipo) {
        grupo g = new grupo();
        g.setNomeGrupo(nome);
        g.setTipoGrupo(tipo);
        grupoRepository.save(g);
    }

    private void criarPrioridade(String nome, int prazoHoras) {
        prioridade p = new prioridade();
        p.setNomePrioridade(nome);
        p.setPrazoHoras(prazoHoras);
        prioridadeRepository.save(p);
    }
}
