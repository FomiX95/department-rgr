const ENTITIES = {
  subjects: {
    title: 'Предмети', one: 'предмет',
    cols: [['name', 'Назва'], ['description', 'Опис']]
  },
  teachers: {
    title: 'Викладачі', one: 'викладача',
    cols: [['fullName', 'ПІБ'], ['jobPosition', 'Посада']],
    parent: { field: 'subjectId', label: 'Предмет', source: 'subjects', text: p => p.name }
  },
  students: {
    title: 'Студенти', one: 'студента',
    cols: [['fullName', 'ПІБ'], ['groupName', 'Група']],
    parent: { field: 'teacherId', label: 'Викладач', source: 'teachers', text: p => p.fullName }
  }
};

async function initList(key, active) {
  const cfg = ENTITIES[key];
  const me = await getMe();
  renderNav(me, active);
  document.getElementById('title').textContent = cfg.title;
  if (me.admin) {
    document.getElementById('actions').innerHTML = `<a class="btn" href="/form.html?e=${key}">+ Додати</a>`;
  }
  const [rows, parents] = await Promise.all([
    api('/api/' + key),
    cfg.parent ? api('/api/' + cfg.parent.source) : Promise.resolve([])
  ]);
  rows.sort((a, b) => a.id - b.id);
  const pmap = {};
  parents.forEach(p => pmap[p.id] = p);
  const parentText = r => {
    const p = pmap[r[cfg.parent.field]];
    return p ? `#${p.id} · ${cfg.parent.text(p)}` : '—';
  };

  const draw = () => {
    const q = document.getElementById('q').value.trim().toLowerCase();
    const shown = rows.filter(r => {
      const hay = r.id + ' ' + cfg.cols.map(c => r[c[0]] ?? '').join(' ') + ' ' + (cfg.parent ? parentText(r) : '');
      return !q || hay.toLowerCase().includes(q);
    });
    document.getElementById('count').textContent = `Записів: ${shown.length} з ${rows.length}`;
    const head = `<tr><th class="id">ID</th>` + cfg.cols.map(c => `<th>${c[1]}</th>`).join('') +
      (cfg.parent ? `<th>${cfg.parent.label}</th>` : '') + (me.admin ? '<th></th>' : '') + `</tr>`;
    const body = shown.map(r => `<tr><td class="id">${r.id}</td>` +
      cfg.cols.map(c => `<td>${esc(r[c[0]] || '—')}</td>`).join('') +
      (cfg.parent ? `<td>${esc(parentText(r))}</td>` : '') +
      (me.admin ? `<td class="act"><a class="btn sm light" href="/form.html?e=${key}&id=${r.id}">Змінити</a> ` +
                  `<button class="btn sm" onclick="delRow('${key}',${r.id})">Видалити</button></td>` : '') +
      `</tr>`).join('');
    document.getElementById('tableBox').innerHTML =
      `<table>${head}${body || '<tr><td colspan="9">Нічого не знайдено</td></tr>'}</table>`;
  };
  document.getElementById('q').addEventListener('input', draw);
  draw();
}

async function delRow(key, id) {
  const extra = key === 'subjects' ? ' Разом із ним буде видалено його викладачів і їхніх студентів.'
              : key === 'teachers' ? ' Разом з ним буде видалено його студентів.' : '';
  if (!confirm(`Видалити запис з ID ${id}?${extra}`)) return;
  await api(`/api/${key}/${id}`, 'DELETE');
  location.reload();
}

async function initForm() {
  const p = new URLSearchParams(location.search);
  const key = p.get('e');
  const id = p.get('id');
  const cfg = ENTITIES[key];
  if (!cfg) { location = '/home.html'; return; }
  const me = await getMe();
  if (!me.admin) { location = '/home.html'; return; }
  renderNav(me, key + '.html');

  const rec = id ? await api(`/api/${key}/${id}`) : {};
  const parents = cfg.parent ? await api('/api/' + cfg.parent.source) : [];
  parents.sort((a, b) => a.id - b.id);

  document.getElementById('title').textContent = id ? `Редагування · ID ${id}` : `Новий запис · ${cfg.one}`;
  let html = cfg.cols.map(([f, l], i) =>
    `<label>${l}</label><input name="${f}" value="${esc(rec[f] || '')}" ${i === 0 ? 'required' : ''}>`).join('');
  if (cfg.parent) {
    const cur = rec[cfg.parent.field] ?? p.get('parent');
    html += `<label>${cfg.parent.label}</label><select name="${cfg.parent.field}" required>` +
      parents.map(x => `<option value="${x.id}" ${String(cur) === String(x.id) ? 'selected' : ''}>` +
        `#${x.id} · ${esc(cfg.parent.text(x))}</option>`).join('') + `</select>`;
  }
  html += `<div class="row"><button class="btn" type="submit">Зберегти</button>` +
          `<a class="btn light" href="/${key}.html">Скасувати</a></div>`;
  const form = document.getElementById('form');
  form.innerHTML = html;
  form.onsubmit = async e => {
    e.preventDefault();
    const body = {};
    cfg.cols.forEach(([f]) => body[f] = form.elements[f].value.trim());
    if (cfg.parent) body[cfg.parent.field] = Number(form.elements[cfg.parent.field].value);
    await api(id ? `/api/${key}/${id}` : `/api/${key}`, id ? 'PUT' : 'POST', body);
    location = `/${key}.html`;
  };
}
