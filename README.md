# To-Do List API

Uma API RESTful para gerenciamento de tarefas com sistema de autenticação JWT.

## 🔐 Autenticação

### Registrar Usuário
```http
POST https://apitodolist-qma3.onrender.com/auth/registro
```

### Fazer Login
```http
POST https://apitodolist-qma3.onrender.com/auth/login
```

## 📊 Endpoints da API Tarefas

### Criar Tarefa
```http
POST https://apitodolist-qma3.onrender.com/api/tarefas
```

### Listar Todas as Tarefas
```http
GET https://apitodolist-qma3.onrender.com/api/tarefas
```

### Filtrar por Status
```http
GET https://apitodolist-qma3.onrender.com/api/tarefas?status=FAZENDO
```

### Listar Tarefas Importantes
```http
GET https://apitodolist-qma3.onrender.com/api/tarefas?importante=true
```

### Buscar Tarefa por ID
```http
GET https://apitodolist-qma3.onrender.com/api/tarefas/{id}
```

### Atualização Parcial
```http
PATCH https://apitodolist-qma3.onrender.com/api/tarefas/{id}
```

### Excluir Tarefa
```http
DELETE https://apitodolist-qma3.onrender.com/api/tarefas/{id}
```

## 🎯 Status Disponíveis
- `A_FAZER`
- `FAZENDO` 
- `FEITO`

## 📅 Formato de Datas
- `yyyy-MM-dd` (ex: `"2025-09-01"`)

---

**URL Base**: `https://apitodolist-qma3.onrender.com` 
