const esc = s => String(s ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));

async function api(url, method = 'GET', body) {
  const r = await fetch(url, {
    method,
    headers: {'Content-Type': 'application/json'},
    body: body ? JSON.stringify(body) : undefined
  });
  if (r.status === 401) { location = '/login.html'; throw new Error('401'); }
  if (!r.ok) { alert('Помилка ' + r.status); throw new Error(String(r.status)); }
  return r.status === 204 ? null : r.json();
}

const getMe = () => api('/api/me');

function renderNav(me, active) {
  const links = [['home.html','Головна'],['subjects.html','Предмети'],['teachers.html','Викладачі'],
                 ['students.html','Студенти'],['app.html','Дерево']];
  document.getElementById('nav').innerHTML =
    `<div class="nav"><div>` +
    links.map(([h, t]) => `<a href="/${h}" class="${h === active ? 'on' : ''}">${t}</a>`).join('') +
    `</div><div class="who">${esc(me.username)} · ${me.admin ? 'ADMIN' : 'USER'}` +
    `<form method="post" action="/logout"><button class="btn sm light">Вийти</button></form></div></div>`;
}
