/* Front-end do Sistema de Chamados — HTML/CSS/JS puro, servido pelo próprio Spring (mesma origem, sem CORS).
   Login: HTTP Basic (CPF + senha), guardado só na sessionStorage da aba. */
const $app = document.getElementById('app');
let auth = sessionStorage.getItem('auth'), me = JSON.parse(sessionStorage.getItem('me') || 'null');

const esc = s => String(s ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const dt = x => x ? new Date(x).toLocaleString('pt-BR', {dateStyle:'short', timeStyle:'short'}) : '—';
const ST = {ABERTO:'Aberto', EM_ATENDIMENTO:'Em atendimento', FECHADO:'Fechado'};
const badge = (t, c = t) => `<span class="b ${esc(c)}">${esc(ST[t] || t)}</span>`;
const staff = () => me && me.perfil !== 'SOLICITANTE';

function toast(m, err) {
  const t = Object.assign(document.createElement('div'), {className: 'toast' + (err ? ' err' : ''), textContent: m});
  document.body.append(t); setTimeout(() => t.remove(), 3500);
}

async function api(path, {method = 'GET', body} = {}) {
  const headers = {'Content-Type': 'application/json'};
  if (auth) headers.Authorization = 'Basic ' + auth;
  const r = await fetch(path, {method, headers, credentials: 'omit', body: body && JSON.stringify(body)});
  const txt = await r.text(); let d = txt;
  try { d = JSON.parse(txt); } catch {}
  if (r.status === 401) { if (me) logout(); throw Error('CPF ou senha inválidos'); }
  if (!r.ok) throw Error(d?.errors?.[0]?.defaultMessage || d?.message || `Erro ${r.status}`);
  return d;
}
// Envia um <form>: converte em objeto, chama fn e mostra erros como toast
window.F = (e, fn) => {
  e.preventDefault();
  fn(Object.fromEntries(new FormData(e.target)), e.target).catch(x => toast(x.message, 1));
};

/* ---------- Autenticação ---------- */
const login = async ({cpf, senha}) => {
  auth = btoa(unescape(encodeURIComponent(cpf.replace(/\D/g, '') + ':' + senha)));
  try { me = await api('/auth/me'); } catch (e) { auth = null; throw e; }
  sessionStorage.setItem('auth', auth); sessionStorage.setItem('me', JSON.stringify(me));
  location.hash = '#/chamados'; route();
};
const cadastrar = async d => {
  await api('/cadastro', {method: 'POST', body: {nomeUsuario: d.nome, idadeUsuario: +d.idade, cepUsuario: d.cep.replace(/\D/g, ''),
    cpfUsuario: d.cpf.replace(/\D/g, ''), senhaUsuario: d.senha}});
  toast('Conta criada! Faça login.'); authView('login');
};
function logout() { sessionStorage.clear(); auth = me = null; location.hash = ''; route(); }
window.logout = logout;

window.authView = (tab = 'login') => {
  $app.innerHTML = `<div class="auth"><h1 style="text-align:center">🎫 Chamados</h1>
  <p class="mut" style="text-align:center">Abra e acompanhe solicitações de suporte</p>
  <div class="card"><div class="tabs"><button class="${tab == 'login' ? '' : 'sec'}" onclick="authView('login')">Entrar</button>
  <button class="${tab == 'login' ? 'sec' : ''}" onclick="authView('cad')">Criar conta</button></div>
  ${tab == 'login' ? `<form onsubmit="F(event,login)"><label>CPF</label><input name="cpf" required inputmode="numeric" placeholder="000.000.000-00">
    <label>Senha</label><input name="senha" type="password" required><button style="width:100%">Entrar</button></form>`
  : `<form onsubmit="F(event,cadastrar)"><label>Nome</label><input name="nome" required>
    <div class="row"><div><label>Idade</label><input name="idade" type="number" min="0" required></div><div><label>CEP</label><input name="cep" required maxlength="9"></div></div>
    <label>CPF</label><input name="cpf" required><label>Senha (mín. 6)</label><input name="senha" type="password" minlength="6" required>
    <button style="width:100%">Criar conta</button></form>`}</div></div>`;
};
window.login = login; window.cadastrar = cadastrar;

/* ---------- Layout e rotas ---------- */
function shell(active, html) {
  const links = [['chamados', 'Chamados'], ['novo', 'Novo chamado'], ['conhecimento', 'Conhecimento']];
  if (staff()) links.splice(2, 0, ['indicadores', 'Indicadores']);
  if (me.perfil === 'ADMIN') links.push(['usuarios', 'Usuários']);
  $app.innerHTML = `<header><div class="bar"><span class="logo">🎫 Chamados</span>
    <nav>${links.map(([h, t]) => `<a href="#/${h}" class="${h == active ? 'on' : ''}">${t}</a>`).join('')}</nav>
    <span class="mut">${esc(me.nomeUsuario)} · ${me.perfil}</span><button class="sec" onclick="logout()">Sair</button></div></header><main>${html}</main>`;
}
async function route() {
  if (!auth || !me) return authView();
  const [, page, id] = (location.hash || '#/chamados').split('/');
  const views = {chamados: lista, novo: novo, chamado: () => detalhe(id), indicadores: indicadores, conhecimento: conhecimento, usuarios: usuarios};
  try { await (views[page] || lista)(); } catch (e) { toast(e.message, 1); }
}
window.addEventListener('hashchange', route);

/* ---------- Chamados ---------- */
let filtro = '';
async function lista() {
  const cs = await api('/chamados' + (filtro ? '?status=' + filtro : ''));
  window.drawLista = q => {
    const rows = cs.filter(c => (c.titulo + c.nomeSolicitante + c.id).toLowerCase().includes(q.toLowerCase()));
    document.getElementById('tb').innerHTML = rows.map(c => {
      const atrasado = c.status !== 'FECHADO' && new Date(c.dataPrazo) < new Date();
      return `<tr class="link" onclick="location.hash='#/chamado/${c.id}'"><td>#${c.id}</td><td><b>${esc(c.titulo)}</b><div class="mut">${esc(c.nomeSolicitante)} · ${esc(c.nomeGrupo)}</div></td>
      <td>${badge(c.status)}</td><td>${badge(c.nomePrioridade)}</td><td>${dt(c.dataPrazo)} ${atrasado ? badge('Atrasado', 'late') : ''}</td></tr>`;
    }).join('') || `<tr><td colspan="5" class="empty">Nenhum chamado encontrado</td></tr>`;
  };
  shell('chamados', `<div class="head"><div><h1>Chamados</h1><div class="mut">${staff() ? 'Todos os chamados' : 'Seus chamados'}</div></div>
    <a class="btn" href="#/novo">+ Novo chamado</a></div>
  <div class="card"><div class="row"><input placeholder="Buscar por título, solicitante ou nº…" oninput="drawLista(this.value)">
    <select onchange="filtro=this.value;route()"><option value="">Todos os status</option>${Object.entries(ST).map(([k, v]) => `<option value="${k}" ${k == filtro ? 'selected' : ''}>${v}</option>`).join('')}</select></div></div>
  <div class="card tw" style="padding:6px"><table><thead><tr><th>Nº</th><th>Chamado</th><th>Status</th><th>Prioridade</th><th>Prazo</th></tr></thead><tbody id="tb"></tbody></table></div>`);
  drawLista('');
}

async function novo() {
  const [gs, ps] = await Promise.all([api('/grupos'), api('/prioridades')]);
  window.abrir = async d => {
    const c = await api('/chamados', {method: 'POST', body: {titulo: d.titulo, descricao: d.descricao, idPrioridade: +d.p || null, idGrupo: +d.g || null}});
    toast('Chamado aberto!'); location.hash = '#/chamado/' + c.id;
  };
  shell('novo', `<h1>Novo chamado</h1><p class="mut">Descreva o problema com o máximo de detalhes.</p><div class="card" style="max-width:700px">
  <form onsubmit="F(event,abrir)"><label>Título</label><input name="titulo" required maxlength="150">
  <label>Descrição</label><textarea name="descricao" required maxlength="2000"></textarea>
  <div class="row"><div><label>Prioridade</label><select name="p"><option value="">Padrão (Média)</option>${ps.map(p => `<option value="${p.id}">${esc(p.nome)} — prazo ${p.prazoHoras}h</option>`).join('')}</select></div>
  <div><label>Grupo</label><select name="g"><option value="">Padrão (N1)</option>${gs.map(g => `<option value="${g.id}">${esc(g.nome)} (${g.tipo.toLowerCase()})</option>`).join('')}</select></div></div>
  <button>Abrir chamado</button></form></div>`);
}

async function detalhe(id) {
  const [c, hist] = await Promise.all([api('/chamados/' + id), api('/historico/chamado/' + id)]);
  const sol = await api('/solucoes/chamado/' + id).catch(() => null);
  const kb = await api('/conhecimento/chamado/' + id).catch(() => []);
  const open = c.status !== 'FECHADO';
  const reload = () => detalhe(id);
  window.assumir = () => api(`/chamados/${id}/assumir`, {method: 'PATCH'}).then(reload).catch(e => toast(e.message, 1));
  window.fechar = () => api(`/chamados/${id}/status`, {method: 'PATCH', body: {status: 'FECHADO', comentario: 'Chamado encerrado'}}).then(reload).catch(e => toast(e.message, 1));
  window.comentar = async (d, f) => { await api('/historico', {method: 'POST', body: {idChamado: +id, idUsuario: me.id, tipoEvento: 'COMENTARIO', descricao: d.t}}); reload(); };
  window.solucionar = async d => { await api('/solucoes', {method: 'POST', body: {idChamado: +id, idUsuario: me.id, descricaoSolucao: d.t}}); reload(); };
  window.paraBase = async () => { await api('/conhecimento', {method: 'POST', body: {titulo: c.titulo, descricaoProblema: c.descricao, descricaoSolucao: sol.descricaoSolucao, idChamado: +id, idUsuario: me.id}}); toast('Salvo na base de conhecimento'); reload(); };
  shell('chamados', `<div class="head"><div><a href="#/chamados" class="mut">← Voltar</a><h1>#${c.id} · ${esc(c.titulo)}</h1>
    <div class="row" style="justify-content:flex-start">${badge(c.status)}${badge(c.nomePrioridade)}</div></div>
    <div class="row" style="flex:0">${staff() && c.status == 'ABERTO' ? `<button onclick="assumir()">Assumir chamado</button>` : ''}
    ${staff() && open && sol ? `<button class="ok" onclick="fechar()">Fechar chamado</button>` : ''}</div></div>
  <div class="two"><div>
    <div class="card"><h2>Descrição</h2><p style="white-space:pre-wrap;margin:0">${esc(c.descricao)}</p></div>
    ${sol ? `<div class="card"><h2>✅ Solução</h2><p style="white-space:pre-wrap">${esc(sol.descricaoSolucao)}</p><div class="mut">Registrada em ${dt(sol.dataRegistro)}</div>
      ${staff() && !kb.length ? `<button class="sec" style="margin-top:10px" onclick="paraBase()">Salvar na base de conhecimento</button>` : ''}</div>`
    : staff() && open ? `<div class="card"><h2>Registrar solução</h2><p class="mut">Obrigatória antes de fechar o chamado.</p><form onsubmit="F(event,solucionar)"><textarea name="t" required></textarea><button>Salvar solução</button></form></div>` : ''}
    <div class="card"><h2>Histórico</h2><div class="tl">${hist.map(h => `<div class="ev"><b>${esc(h.tipoEvento.replace('_', ' '))}</b> <span class="mut">· ${dt(h.dataRegistro)}</span>
      <div>${esc(h.descricao)}</div></div>`).join('') || '<div class="mut">Sem eventos</div>'}</div>
      ${open ? `<form onsubmit="F(event,comentar)" style="margin-top:12px"><label>Adicionar comentário</label><textarea name="t" required style="min-height:70px"></textarea><button class="sec">Comentar</button></form>` : ''}</div>
  </div><div class="card"><h2>Detalhes</h2><dl class="kv"><dt>Solicitante</dt><dd>${esc(c.nomeSolicitante)}</dd><dt>Técnico</dt><dd>${esc(c.nomeTecnico || 'Não atribuído')}</dd>
    <dt>Grupo</dt><dd>${esc(c.nomeGrupo)}</dd><dt>Aberto em</dt><dd>${dt(c.dataAbertura)}</dd><dt>Prazo</dt><dd>${dt(c.dataPrazo)}</dd>
    <dt>Encerrado</dt><dd>${dt(c.dataEncerramento)}</dd><dt>SLA</dt><dd>${c.slaCumprido == null ? '—' : c.slaCumprido ? badge('Cumprido', 'ok') : badge('Estourado', 'late')}</dd></dl></div></div>`);
}

/* ---------- Indicadores ---------- */
async function indicadores() {
  const [tipo, prio, grp, sla] = await Promise.all(['por-tipo', 'por-prioridade', 'por-grupo', 'sla'].map(p => api('/indicadores/' + p)));
  const bars = (t, it) => { const mx = Math.max(1, ...it.map(i => i.quantidade));
    return `<div class="card"><h2>${t}</h2>${it.map(i => `<div class="m"><span>${esc(i.agrupamento)}</span><div class="bar2" style="width:${i.quantidade / mx * 100}%;flex:none;max-width:55%"></div>
      <b>${i.quantidade}</b><span class="mut">${i.tempoMedioMinutos != null ? Math.round(i.tempoMedioMinutos) + ' min' : ''}</span></div>`).join('') || '<div class="empty">Sem dados</div>'}</div>`; };
  const pct = Math.round(sla.percentualCumprido || 0);
  shell('indicadores', `<h1>Indicadores</h1><p class="mut">Quantidade de chamados fechados e tempo médio de atendimento.</p><div class="grid">
    <div class="card"><h2>SLA</h2><div class="donut" style="background:conic-gradient(var(--ok) ${pct}%,#fde2e2 0)"><div>${pct}%</div></div>
    <p class="mut" style="text-align:center">${sla.cumpridos} cumpridos · ${sla.estourados} estourados · ${sla.total} no total</p></div>
    ${bars('Por tipo', tipo)}${bars('Por prioridade', prio)}${bars('Por grupo', grp)}</div>`);
}

/* ---------- Base de conhecimento ---------- */
async function conhecimento() {
  window.buscarKb = async q => { const a = await api('/conhecimento?busca=' + encodeURIComponent(q));
    document.getElementById('kb').innerHTML = a.map(k => `<div class="card"><h2>${esc(k.titulo)}</h2><b class="mut">Problema</b><p style="white-space:pre-wrap">${esc(k.descricaoProblema)}</p>
      <b class="mut">Solução</b><p style="white-space:pre-wrap;margin-bottom:0">${esc(k.descricaoSolucao)}</p></div>`).join('') || '<div class="empty">Nenhum artigo encontrado</div>'; };
  window.novoKb = async (d, f) => { await api('/conhecimento', {method: 'POST', body: {titulo: d.titulo, descricaoProblema: d.p, descricaoSolucao: d.s, idUsuario: me.id}}); toast('Artigo salvo'); route(); };
  shell('conhecimento', `<div class="head"><h1>Base de conhecimento</h1></div><div class="card"><input placeholder="Buscar artigos…" oninput="buscarKb(this.value)"></div><div id="kb"></div>
  ${staff() ? `<details class="card"><summary><b>+ Novo artigo</b></summary><form onsubmit="F(event,novoKb)"><label>Título</label><input name="titulo" required>
    <label>Problema</label><textarea name="p" required></textarea><label>Solução</label><textarea name="s" required></textarea><button>Salvar artigo</button></form></details>` : ''}`);
  buscarKb('');
}

/* ---------- Usuários (ADMIN) ---------- */
async function usuarios() {
  const us = await api('/usuarios');
  window.setPerfil = (id, perfil) => api(`/usuarios/${id}/perfil`, {method: 'PATCH', body: {perfil}}).then(() => toast('Perfil atualizado')).catch(e => { toast(e.message, 1); route(); });
  window.setAtivo = (id, ativo) => api(`/usuarios/${id}/status`, {method: 'PATCH', body: {ativo}}).then(route).catch(e => toast(e.message, 1));
  shell('usuarios', `<h1>Usuários</h1><div class="card tw" style="padding:6px"><table><thead><tr><th>Nome</th><th>Perfil</th><th>Status</th><th></th></tr></thead><tbody>
  ${us.map(u => `<tr><td><b>${esc(u.nomeUsuario)}</b><div class="mut">#${u.id} · ${u.idadeUsuario} anos · CEP ${esc(u.cepUsuario)}</div></td>
  <td><select onchange="setPerfil(${u.id},this.value)">${['SOLICITANTE', 'TECNICO', 'ADMIN'].map(p => `<option ${p == u.perfil ? 'selected' : ''}>${p}</option>`).join('')}</select></td>
  <td>${u.ativo ? badge('Ativo', 'ok') : badge('Inativo', 'late')}</td><td><button class="sec" onclick="setAtivo(${u.id},${!u.ativo})">${u.ativo ? 'Desativar' : 'Ativar'}</button></td></tr>`).join('')}</tbody></table></div>`);
}

route();
