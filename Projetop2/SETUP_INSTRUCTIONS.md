# Instruções de Configuração

## 📥 Download das Dependências

Para executar o sistema, você precisa baixar as seguintes bibliotecas e colocá-las na pasta `lib/`:

### 1. MySQL Connector/J
- **URL**: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.2.0/mysql-connector-j-9.2.0.jar
- **Arquivo**: `mysql-connector-j-9.2.0.jar`

### 2. FlatLaf
- **URL**: https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2.1/flatlaf-3.2.1.jar
- **Arquivo**: `flatlaf-3.2.1.jar`

## 📁 Estrutura de Pastas

Após baixar as dependências, sua estrutura deve ficar assim:

```
Projetop2/
├── lib/
│   ├── mysql-connector-j-9.2.0.jar
│   └── flatlaf-3.2.1.jar
├── src/
├── target/
├── run.bat
├── database_setup.sql
└── README.md
```

## 🗄️ Configuração do Banco de Dados

1. **Instale o MySQL** (se ainda não tiver)
2. **Execute o script SQL**:
   ```bash
   mysql -u root -p < database_setup.sql
   ```
3. **Configure a conexão** no arquivo `src/main/java/factory/ConnectionFactory.java`:
   ```java
   private static final String usuario = "seu_usuario";
   private static final String senha = "sua_senha";
   ```

## 🚀 Execução

### Opção 1: Usando o arquivo batch (Windows)
```bash
run.bat
```

### Opção 2: Compilação manual
```bash
# Compilar
javac -cp "lib/*" -d target/classes src/main/java/factory/*.java
javac -cp "lib/*" -d target/classes src/main/java/modelo/*.java
javac -cp "lib/*" -d target/classes src/main/java/dao/*.java
javac -cp "lib/*" -d target/classes src/main/java/gui/util/*.java
javac -cp "lib/*" -d target/classes src/main/java/gui/*.java

# Executar
java -cp "lib/*;target/classes" gui.TelaPrincipal
```

### Opção 3: Usando Maven (se instalado)
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="gui.TelaPrincipal"
```

## 🔧 Solução de Problemas

### Erro "Class not found"
- Verifique se as dependências estão na pasta `lib/`
- Confirme se os nomes dos arquivos estão corretos

### Erro de conexão com banco
- Verifique se o MySQL está rodando
- Confirme as credenciais no `ConnectionFactory.java`
- Execute o script `database_setup.sql`

### Erro de compilação
- Certifique-se de ter Java 23+ instalado
- Verifique se todas as dependências estão presentes

## 📞 Suporte

Se encontrar problemas, verifique:
1. Versão do Java (deve ser 23+)
2. Dependências na pasta `lib/`
3. Configuração do banco de dados
4. Permissões de arquivo 