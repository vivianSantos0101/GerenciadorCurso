# Sistema de Gestão de Cursos e Alunos

Um sistema completo de CRUD (Create, Read, Update, Delete) para gerenciamento de cursos e alunos, desenvolvido em Java com interface gráfica moderna.

## 🚀 Funcionalidades

### 📚 Gestão de Cursos
- ✅ Cadastro de novos cursos
- ✅ Edição de cursos existentes
- ✅ Exclusão de cursos
- ✅ Visualização em tabela organizada
- ✅ Validação de dados
- ✅ Status ativo/inativo

### 👥 Gestão de Alunos
- ✅ Cadastro de novos alunos
- ✅ Edição de dados dos alunos
- ✅ Exclusão de alunos
- ✅ Validação de CPF
- ✅ Validação de email
- ✅ Validação de idade (mínimo 16 anos)
- ✅ Associação com cursos
- ✅ Status ativo/inativo

### 📊 Relatórios e Estatísticas
- ✅ Relatórios por status (ativos/inativos)
- ✅ Estatísticas gerais
- ✅ Exportação para CSV
- ✅ Filtros por categoria
- ✅ Visualização em tempo real

## 🛠️ Tecnologias Utilizadas

- **Java 23** - Linguagem principal
- **Swing** - Interface gráfica
- **FlatLaf** - Tema moderno para Swing
- **MySQL** - Banco de dados
- **Maven** - Gerenciamento de dependências

## 📋 Pré-requisitos

- Java 23 ou superior
- MySQL 8.0 ou superior
- Maven 3.6 ou superior

## 🗄️ Configuração do Banco de Dados

### 1. Criar o banco de dados:
```sql
CREATE DATABASE gerenciadorcurso;
USE gerenciadorcurso;
```

### 2. Criar as tabelas:
```sql
-- Tabela de Cursos
CREATE TABLE Curso (
    ID_Curso INT AUTO_INCREMENT PRIMARY KEY,
    Nome_Curso VARCHAR(50) NOT NULL,
    Carga_Horaria INT NOT NULL,
    Limite_Alunos INT NOT NULL,
    Ativo_Curso BOOLEAN DEFAULT TRUE
);

-- Tabela de Alunos
CREATE TABLE Aluno (
    ID_Aluno INT AUTO_INCREMENT PRIMARY KEY,
    CPF_Aluno VARCHAR(14) NOT NULL UNIQUE,
    Nome_Aluno VARCHAR(100) NOT NULL,
    Email_Aluno VARCHAR(100) NOT NULL,
    Data_Nasc DATE NOT NULL,
    Ativo_Aluno BOOLEAN DEFAULT TRUE,
    ID_Curso INT,
    FOREIGN KEY (ID_Curso) REFERENCES Curso(ID_Curso)
);
```

### 3. Configurar conexão:
Edite o arquivo `src/main/java/factory/ConnectionFactory.java` com suas credenciais:
```java
private static final String url = "jdbc:mysql://localhost:3306/gerenciadorcurso";
private static final String usuario = "seu_usuario";
private static final String senha = "sua_senha";
```

## 🚀 Como Executar

### 1. Compilar o projeto:
```bash
mvn clean compile
```

### 2. Executar a aplicação:
```bash
mvn exec:java -Dexec.mainClass="gui.TelaPrincipal"
```

Ou execute diretamente a classe `TelaPrincipal` em sua IDE.

## 🎨 Interface Moderna

### Características da Interface:
- **Design Responsivo**: Adapta-se a diferentes tamanhos de tela
- **Tema Moderno**: Utiliza FlatLaf para aparência contemporânea
- **Cores Harmoniosas**: Paleta de cores profissional
- **Ícones Intuitivos**: Emojis e símbolos para melhor UX
- **Validação Visual**: Feedback imediato para o usuário
- **Navegação Intuitiva**: Abas organizadas e botões claros

### Componentes Estilizados:
- Botões com cores temáticas (primário, sucesso, perigo)
- Campos de texto com bordas modernas
- Tabelas com cabeçalhos destacados
- Painéis com sombras e bordas arredondadas
- Tipografia consistente (Segoe UI)

## ✅ Validações Implementadas

### Para Cursos:
- Nome: 3-50 caracteres
- Carga horária: 20-1000 horas
- Limite de alunos: 1-500

### Para Alunos:
- Nome: 3-100 caracteres
- Email: formato válido
- CPF: formato XXX.XXX.XXX-XX e algoritmo de validação
- Idade: mínimo 16 anos, máximo 100 anos
- Curso: obrigatório

## 📁 Estrutura do Projeto

```
src/main/java/
├── dao/                    # Camada de acesso a dados
│   ├── AlunoDAO.java      # Operações CRUD para alunos
│   └── CursoDAO.java      # Operações CRUD para cursos
├── factory/               # Configurações
│   └── ConnectionFactory.java  # Conexão com banco
├── gui/                   # Interface gráfica
│   ├── util/              # Utilitários da interface
│   │   ├── StyledComponents.java  # Componentes estilizados
│   │   └── ValidationUtil.java    # Validações
│   ├── AlunoPainel.java   # Painel de alunos
│   ├── CursoPainel.java   # Painel de cursos
│   ├── RelatorioPainel.java # Painel de relatórios
│   └── TelaPrincipal.java # Janela principal
└── modelo/                # Classes de modelo
    ├── Aluno.java         # Modelo de aluno
    └── Curso.java         # Modelo de curso
```

## 🔧 Melhorias Implementadas

### 1. **Correção de Bugs**:
- Removido método `editar` incorreto do `AlunoDAO`
- Implementado método `listarPorStatus` no `AlunoDAO`
- Adicionado método `buscarPorId` em ambos os DAOs
- Corrigidas consultas SQL com JOINs apropriados

### 2. **Interface Moderna**:
- Tema FlatLaf Light para aparência moderna
- Componentes estilizados com cores profissionais
- Layout responsivo com GridBagLayout
- Ícones e emojis para melhor UX

### 3. **Funcionalidades Avançadas**:
- CRUD completo (Create, Read, Update, Delete)
- Validação robusta de dados
- Relatórios com estatísticas
- Exportação para TXT
- Filtros e busca

### 4. **Validações Robustas**:
- Validação de CPF com algoritmo oficial
- Validação de email com regex
- Validação de idade
- Validação de campos obrigatórios

### 5. **Experiência do Usuário**:
- Feedback visual imediato
- Mensagens de erro claras
- Confirmações para ações destrutivas
- Interface intuitiva e responsiva

## 📊 Funcionalidades dos Relatórios

- **Estatísticas Gerais**: Total de alunos, ativos/inativos
- **Distribuição por Curso**: Quantidade de alunos por curso
- **Filtros**: Por status, por curso
- **Exportação**: Formato TXT com dados completos e estatísticas
- **Atualização em Tempo Real**: Dados sempre atualizados

## 🎯 Como Usar

1. **Cadastrar um Curso**:
   - Vá para a aba "📚 Cursos"
   - Preencha os dados do curso
   - Clique em "💾 Salvar"

2. **Cadastrar um Aluno**:
   - Vá para a aba "👥 Alunos"
   - Preencha todos os dados (CPF no formato XXX.XXX.XXX-XX)
   - Selecione um curso
   - Clique em "💾 Salvar"

3. **Editar Registros**:
   - Selecione um item na tabela
   - Os dados aparecerão no formulário
   - Faça as alterações
   - Clique em "✏️ Editar"

4. **Excluir Registros**:
   - Selecione um item na tabela
   - Clique em "🗑️ Excluir"
   - Confirme a exclusão

5. **Gerar Relatórios**:
   - Vá para a aba "📊 Relatórios"
   - Escolha o filtro desejado
   - Clique em "📊 Gerar Relatório"
   - Para exportar, clique em "📄 Exportar TXT"

## 🐛 Solução de Problemas

### Erro de Conexão com Banco:
- Verifique se o MySQL está rodando
- Confirme as credenciais no `ConnectionFactory.java`
- Verifique se o banco `gerenciadorcurso` existe

### Erro de Compilação:
- Certifique-se de ter Java 23+ instalado
- Execute `mvn clean compile` para limpar e recompilar

### Interface não carrega:
- Verifique se o FlatLaf está no classpath
- Tente executar com tema do sistema como fallback

## 📝 Licença

Este projeto foi desenvolvido como atividade acadêmica.

## 👨‍💻 Desenvolvido por

Sistema de Gestão de Cursos e Alunos - Versão 1.0 