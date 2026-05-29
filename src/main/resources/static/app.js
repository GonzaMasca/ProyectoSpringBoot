const API_ALUMNOS = '/api/v1/alumnos';
const API_CAPACITACIONES = '/api/v1/capacitaciones';
const API_PROFESORES = '/api/v1/profesores';

// Inicializar cargas de datos
document.addEventListener('DOMContentLoaded', () => {
    switchTab('dashboard');

    // Cargar CSRF token para que funcione el botón de logout
    fetch('/api/auth/csrf')
        .then(r => r.json())
        .then(data => {
            const csrfInput = document.getElementById('csrfToken');
            if (csrfInput && data.token) {
                csrfInput.value = data.token;
                csrfInput.name  = data.paramName || '_csrf';
            }
        })
        .catch(() => { /* Si falla, el logout igualmente funciona via redirect */ });

    // Configurar listeners de envío de formularios
    document.getElementById('aluForm').addEventListener('submit', guardarAlumno);
    document.getElementById('capForm').addEventListener('submit', guardarCapacitacion);
    document.getElementById('profForm').addEventListener('submit', guardarProfesor);
});

// Alternar entre pestañas de navegación
function switchTab(tabName) {
    document.querySelectorAll('.nav-tab').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(sec => sec.classList.remove('active'));

    const activeBtn = Array.from(document.querySelectorAll('.nav-tab')).find(btn => btn.getAttribute('onclick').includes(tabName));
    if (activeBtn) activeBtn.classList.add('active');

    const activeSec = document.getElementById(`tab-${tabName}`);
    if (activeSec) activeSec.classList.add('active');

    // Cargar datos por pestaña
    if (tabName === 'dashboard') {
        cargarDashboard();
    } else if (tabName === 'alumnos') {
        cargarAlumnos();
        cargarCapacitacionesPills();
    } else if (tabName === 'capacitaciones') {
        cargarCapacitaciones();
        cargarProfesoresSelect();
    } else if (tabName === 'profesores') {
        cargarProfesores();
    }
}

// Mostrar notificaciones flotantes (Toasts)
function showToast(message, isError = false) {
    const toast = document.getElementById('toast');
    const toastMsg = document.getElementById('toastMessage');
    
    toastMsg.innerText = message;
    
    if (isError) {
        toast.classList.add('error');
        toast.querySelector('i').className = 'fa-solid fa-circle-xmark';
    } else {
        toast.classList.remove('error');
        toast.querySelector('i').className = 'fa-solid fa-circle-check';
    }
    
    toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), 3500);
}

// Resetear y Cancelar Edición
function cancelarEdicion(prefijo) {
    document.getElementById(`${prefijo}Form`).reset();
    document.getElementById(`${prefijo}Id`).value = '';
    document.getElementById(`${prefijo}BtnSubmit`).innerHTML = '<i class="fa-solid fa-floppy-disk"></i> Guardar ' + (prefijo === 'alu' ? 'Alumno' : prefijo === 'cap' ? 'Curso' : 'Profesor');
    document.getElementById(`${prefijo}FormTitle`).innerHTML = `<i class="fa-solid ${prefijo === 'alu' ? 'fa-user-plus' : prefijo === 'cap' ? 'fa-circle-plus' : 'fa-user-tie'}"></i> Registrar ` + (prefijo === 'alu' ? 'Estudiante' : prefijo === 'cap' ? 'Curso' : 'Profesor');
    document.getElementById(`${prefijo}BtnCancel`).style.display = 'none';
}

/* =========================================================================
   SECCIÓN: DASHBOARD / RESUMEN
   ========================================================================= */
async function cargarDashboard() {
    try {
        // Realizar peticiones en paralelo
        const [resAlu, resCap, resProf] = await Promise.all([
            fetch(API_ALUMNOS),
            fetch(API_CAPACITACIONES),
            fetch(API_PROFESORES)
        ]);

        const alumnos = await resAlu.json();
        const capacitaciones = await resCap.json();
        const profesores = await resProf.json();

        // Cargar números rápidos
        document.getElementById('stat-alumnos').innerText = alumnos.length;
        document.getElementById('stat-cursos').innerText = capacitaciones.length;
        document.getElementById('stat-profesores').innerText = profesores.length;

        // Cargar tabla de ocupación
        const tbody = document.getElementById('occupancyTableBody');
        tbody.innerHTML = '';

        if (capacitaciones.length === 0) {
            tbody.innerHTML = `<tr><td colspan="4" class="no-data">No hay cursos registrados todavía.</td></tr>`;
            return;
        }

        capacitaciones.forEach(c => {
            const docenteName = c.profesor ? `${c.profesor.nombre} ${c.profesor.apellido}` : 'No asignado';
            const docenteEsp = c.profesor ? c.profesor.especialidad : 'N/A';
            
            // Contar alumnos inscritos en este curso
            const alumnosInscritos = alumnos.filter(alu => 
                alu.capacitaciones && alu.capacitaciones.some(cap => cap.id_cap === c.id_cap)
            ).length;

            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong>${c.nombre}</strong></td>
                <td>${docenteName}</td>
                <td>${docenteEsp}</td>
                <td><span class="tag tag-special">${alumnosInscritos} Estudiantes</span></td>
            `;
            tbody.appendChild(tr);
        });

    } catch (error) {
        console.error("Error al cargar el dashboard:", error);
        showToast("Error al obtener estadísticas del sistema", true);
    }
}

/* =========================================================================
   SECCIÓN: ALUMNOS (ESTUDIANTES)
   ========================================================================= */

async function cargarAlumnos() {
    try {
        const response = await fetch(API_ALUMNOS);
        if (!response.ok) throw new Error('Error al conectar con la API de alumnos');
        const alumnos = await response.json();

        const grid = document.getElementById('aluGrid');
        grid.innerHTML = '';

        if (alumnos.length === 0) {
            grid.innerHTML = `
                <div class="no-data">
                    <i class="fa-solid fa-graduation-cap"></i>
                    <p>No hay alumnos en la base de datos.</p>
                </div>
            `;
            return;
        }

        alumnos.forEach(alu => {
            const card = document.createElement('div');
            card.className = 'item-card';
            const iniciales = alu.nombre.split(' ').map(n => n[0]).join('').slice(0, 2).toUpperCase();

            const tags = alu.capacitaciones && alu.capacitaciones.length > 0
                ? alu.capacitaciones.map(c => `<span class="tag">${c.nombre} (Prof. ${c.profesor ? c.profesor.apellido : 'N/A'})</span>`).join('')
                : '<span style="color: var(--text-muted); font-size: 0.75rem; font-style: italic;">Sin capacitaciones</span>';

            card.innerHTML = `
                <div class="item-info">
                    <div class="item-avatar">${iniciales}</div>
                    <div class="item-details">
                        <h3>${alu.nombre}</h3>
                        <div class="item-subtext">Usuario: <span class="item-subtext-badge">@${alu.pseudonimo}</span></div>
                        <div class="item-tags">${tags}</div>
                    </div>
                </div>
                <div class="item-actions">
                    <button onclick='iniciarEdicionAlumno(${JSON.stringify(alu)})' class="btn-action btn-action-edit" title="Editar">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </button>
                    <button onclick="eliminarAlumno(${alu.id_alu})" class="btn-action btn-action-delete" title="Eliminar">
                        <i class="fa-solid fa-trash-can"></i>
                    </button>
                </div>
            `;
            grid.appendChild(card);
        });
    } catch (error) {
        console.error(error);
        document.getElementById('aluGrid').innerHTML = `<div class="no-data" style="color: #ef4444;"><p>Error al cargar alumnos.</p></div>`;
    }
}

async function cargarCapacitacionesPills() {
    try {
        const response = await fetch(API_CAPACITACIONES);
        const caps = await response.json();
        const container = document.getElementById('aluCapsContainer');
        container.innerHTML = '';

        if (caps.length === 0) {
            container.innerHTML = '<span style="color: var(--text-muted); font-size: 0.85rem;">Primero debes crear cursos en la pestaña "Cursos".</span>';
            return;
        }

        caps.forEach(c => {
            const div = document.createElement('div');
            div.className = 'pill-checkbox';
            div.innerHTML = `
                <input type="checkbox" name="aluCaps" id="check_cap_${c.id_cap}" value="${c.id_cap}" data-name="${c.nombre}">
                <label for="check_cap_${c.id_cap}" class="pill-label">
                    <i class="fa-solid fa-bookmark" style="margin-right: 6px; font-size: 0.8rem; opacity: 0.7;"></i>
                    ${c.nombre}
                </label>
            `;
            container.appendChild(div);
        });
    } catch (error) {
        console.error(error);
    }
}

function iniciarEdicionAlumno(alu) {
    document.getElementById('aluId').value = alu.id_alu;
    document.getElementById('aluNombre').value = alu.nombre;
    document.getElementById('aluPseudo').value = alu.pseudonimo;
    
    // Marcar checkbox
    document.querySelectorAll('input[name="aluCaps"]').forEach(chk => {
        chk.checked = alu.capacitaciones.some(c => c.id_cap == chk.value);
    });

    document.getElementById('aluBtnSubmit').innerHTML = '<i class="fa-solid fa-pen-to-square"></i> Guardar Cambios';
    document.getElementById('aluFormTitle').innerHTML = '<i class="fa-solid fa-user-pen"></i> Editar Estudiante';
    document.getElementById('aluBtnCancel').style.display = 'block';
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function guardarAlumno(e) {
    e.preventDefault();
    const id = document.getElementById('aluId').value;
    const nombre = document.getElementById('aluNombre').value.trim();
    const pseudonimo = document.getElementById('aluPseudo').value.trim();

    const checkboxes = document.querySelectorAll('input[name="aluCaps"]:checked');
    const capacitaciones = Array.from(checkboxes).map(chk => ({
        id_cap: parseInt(chk.value),
        nombre: chk.getAttribute('data-name')
    }));

    const payload = { nombre, pseudonimo, capacitaciones };
    const url = id ? `${API_ALUMNOS}/${id}` : API_ALUMNOS;
    const method = id ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const data = await response.json();
            throw new Error(data.error || 'Error al guardar los datos.');
        }

        showToast(id ? '¡Estudiante actualizado con éxito!' : '¡Estudiante registrado con éxito!');
        cancelarEdicion('alu');
        cargarAlumnos();
        cargarCapacitacionesPills();
    } catch (error) {
        showToast(error.message, true);
    }
}

async function eliminarAlumno(id) {
    if (!confirm('¿Estás seguro de que deseas eliminar este alumno?')) return;

    try {
        const response = await fetch(`${API_ALUMNOS}/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('No se pudo eliminar el alumno.');

        showToast('Estudiante eliminado correctamente.');
        cargarAlumnos();
    } catch (error) {
        showToast(error.message, true);
    }
}

/* =========================================================================
   SECCIÓN: CAPACITACIONES (CURSOS)
   ========================================================================= */

async function cargarCapacitaciones() {
    try {
        const response = await fetch(API_CAPACITACIONES);
        const caps = await response.json();

        const grid = document.getElementById('capGrid');
        grid.innerHTML = '';

        if (caps.length === 0) {
            grid.innerHTML = `
                <div class="no-data">
                    <i class="fa-solid fa-book-open"></i>
                    <p>No hay cursos ofertados.</p>
                </div>
            `;
            return;
        }

        caps.forEach(c => {
            const card = document.createElement('div');
            card.className = 'item-card';

            const profInfo = c.profesor 
                ? `<span class="tag tag-special"><i class="fa-solid fa-user-tie"></i> Prof. ${c.profesor.nombre} ${c.profesor.apellido}</span>` 
                : '<span style="color:#ef4444; font-size: 0.75rem;">Sin profesor asignado</span>';

            card.innerHTML = `
                <div class="item-info">
                    <div class="item-avatar" style="background-color: #f1f5f9; color: var(--text-secondary);">
                        <i class="fa-solid fa-bookmark" style="font-size: 0.85rem;"></i>
                    </div>
                    <div class="item-details">
                        <h3>${c.nombre}</h3>
                        <div class="item-tags">${profInfo}</div>
                    </div>
                </div>
                <div class="item-actions">
                    <button onclick='iniciarEdicionCapacitacion(${JSON.stringify(c)})' class="btn-action btn-action-edit" title="Editar">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </button>
                    <button onclick="eliminarCapacitacion(${c.id_cap})" class="btn-action btn-action-delete" title="Eliminar">
                        <i class="fa-solid fa-trash-can"></i>
                    </button>
                </div>
            `;
            grid.appendChild(card);
        });
        
        // Actualizar también la selección de pills de capacitaciones en alumnos
        cargarCapacitacionesPills();
    } catch (error) {
        console.error(error);
    }
}

async function cargarProfesoresSelect() {
    try {
        const response = await fetch(API_PROFESORES);
        const profesores = await response.json();
        
        const select = document.getElementById('capProfesor');
        select.innerHTML = '<option value="" disabled selected>Selecciona un Profesor</option>';

        profesores.forEach(p => {
            const opt = document.createElement('option');
            opt.value = p.id_prof;
            opt.textContent = `${p.nombre} ${p.apellido} (${p.especialidad})`;
            select.appendChild(opt);
        });
    } catch (error) {
        console.error(error);
    }
}

function iniciarEdicionCapacitacion(c) {
    document.getElementById('capId').value = c.id_cap;
    document.getElementById('capNombre').value = c.nombre;
    
    // Esperar que carguen los profesores
    cargarProfesoresSelect().then(() => {
        if (c.profesor) {
            document.getElementById('capProfesor').value = c.profesor.id_prof;
        }
    });

    document.getElementById('capBtnSubmit').innerHTML = '<i class="fa-solid fa-pen-to-square"></i> Guardar Cambios';
    document.getElementById('capFormTitle').innerHTML = '<i class="fa-solid fa-pen-nib"></i> Editar Curso';
    document.getElementById('capBtnCancel').style.display = 'block';
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function guardarCapacitacion(e) {
    e.preventDefault();
    const id = document.getElementById('capId').value;
    const nombre = document.getElementById('capNombre').value.trim();
    const profId = document.getElementById('capProfesor').value;

    if (!profId) {
        showToast('Debe seleccionar un profesor obligatoriamente.', true);
        return;
    }

    const payload = {
        nombre: nombre,
        profesor: { id_prof: parseInt(profId) }
    };

    const url = id ? `${API_CAPACITACIONES}/${id}` : API_CAPACITACIONES;
    const method = id ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const data = await response.json();
            throw new Error(data.error || 'Error al guardar los datos.');
        }

        showToast(id ? '¡Curso actualizado con éxito!' : '¡Curso registrado con éxito!');
        cancelarEdicion('cap');
        cargarCapacitaciones();
    } catch (error) {
        showToast(error.message, true);
    }
}

async function eliminarCapacitacion(id) {
    if (!confirm('¿Estás seguro de que deseas eliminar este curso?')) return;

    try {
        const response = await fetch(`${API_CAPACITACIONES}/${id}`, { method: 'DELETE' });
        
        if (!response.ok) {
            const data = await response.json();
            throw new Error(data.error || 'No se pudo eliminar el curso.');
        }

        showToast('Curso eliminado correctamente.');
        cargarCapacitaciones();
    } catch (error) {
        showToast(error.message, true);
    }
}

/* =========================================================================
   SECCIÓN: PROFESORES
   ========================================================================= */

async function cargarProfesores() {
    try {
        const response = await fetch(API_PROFESORES);
        const profesores = await response.json();

        const grid = document.getElementById('profGrid');
        grid.innerHTML = '';

        if (profesores.length === 0) {
            grid.innerHTML = `
                <div class="no-data">
                    <i class="fa-solid fa-user-slash"></i>
                    <p>No hay profesores en la lista docente.</p>
                </div>
            `;
            return;
        }

        profesores.forEach(p => {
            const card = document.createElement('div');
            card.className = 'item-card';
            const iniciales = p.nombre[0].toUpperCase() + p.apellido[0].toUpperCase();

            card.innerHTML = `
                <div class="item-info">
                    <div class="item-avatar">
                        ${iniciales}
                    </div>
                    <div class="item-details">
                        <h3>${p.nombre} ${p.apellido}</h3>
                        <div class="item-subtext">Especialidad: <span class="item-subtext-badge">${p.especialidad}</span></div>
                    </div>
                </div>
                <div class="item-actions">
                    <button onclick='iniciarEdicionProfesor(${JSON.stringify(p)})' class="btn-action btn-action-edit" title="Editar">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </button>
                    <button onclick="eliminarProfesor(${p.id_prof})" class="btn-action btn-action-delete" title="Eliminar">
                        <i class="fa-solid fa-trash-can"></i>
                    </button>
                </div>
            `;
            grid.appendChild(card);
        });
        
        // Actualizar la lista desplegable de profesores en la pestaña capacitaciones
        cargarProfesoresSelect();
    } catch (error) {
        console.error(error);
    }
}

function iniciarEdicionProfesor(p) {
    document.getElementById('profId').value = p.id_prof;
    document.getElementById('profNombre').value = p.nombre;
    document.getElementById('profApellido').value = p.apellido;
    document.getElementById('profEspecialidad').value = p.especialidad;

    document.getElementById('profBtnSubmit').innerHTML = '<i class="fa-solid fa-pen-to-square"></i> Guardar Cambios';
    document.getElementById('profFormTitle').innerHTML = '<i class="fa-solid fa-user-gear"></i> Editar Profesor';
    document.getElementById('profBtnCancel').style.display = 'block';
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function guardarProfesor(e) {
    e.preventDefault();
    const id = document.getElementById('profId').value;
    const nombre = document.getElementById('profNombre').value.trim();
    const apellido = document.getElementById('profApellido').value.trim();
    const especialidad = document.getElementById('profEspecialidad').value.trim();

    const payload = { nombre, apellido, especialidad };
    const url = id ? `${API_PROFESORES}/${id}` : API_PROFESORES;
    const method = id ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const data = await response.json();
            throw new Error(data.error || 'Error al guardar los datos.');
        }

        showToast(id ? '¡Profesor actualizado con éxito!' : '¡Profesor registrado con éxito!');
        cancelarEdicion('prof');
        cargarProfesores();
    } catch (error) {
        showToast(error.message, true);
    }
}

async function eliminarProfesor(id) {
    if (!confirm('¿Estás seguro de que deseas eliminar este profesor?')) return;

    try {
        const response = await fetch(`${API_PROFESORES}/${id}`, { method: 'DELETE' });
        
        if (!response.ok) {
            const data = await response.json();
            throw new Error(data.error || 'No se pudo eliminar el profesor.');
        }

        showToast('Profesor eliminado correctamente.');
        cargarProfesores();
    } catch (error) {
        showToast(error.message, true);
    }
}
