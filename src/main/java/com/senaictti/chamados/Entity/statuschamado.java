package com.senaictti.chamados.Entity;

/*
 Ciclo de vida do chamado:
   ABERTO -> EM_ATENDIMENTO -> FECHADO
 (EM_ATENDIMENTO pode voltar para ABERTO. FECHADO é final.)
 Para fechar é obrigatório existir uma solução registrada.
 */
public enum statuschamado {
    ABERTO,
    EM_ATENDIMENTO,
    FECHADO
}
