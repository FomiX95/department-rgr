(async function () {
  const me = await getMe();
  renderNav(me, 'app.html');
  const tree = await api('/api/tree');
  const tag = id => `<span class="tag">ID ${id}</span>`;
  document.getElementById('tree').innerHTML = tree.map(s => `
    <div class="s">${tag(s.id)}${esc(s.name)} <span class="muted">${esc(s.description)}</span></div>
    <ul>${s.teachers.map(t => `
      <li class="t">${tag(t.id)}${esc(t.fullName)} <span class="muted">${esc(t.jobPosition)}</span>
        <ul>${t.students.map(st => `
          <li class="st">${tag(st.id)}${esc(st.fullName)} <span class="muted">${esc(st.groupName)}</span></li>`).join('')}</ul>
      </li>`).join('')}</ul>`).join('') || 'Даних немає';
})();
