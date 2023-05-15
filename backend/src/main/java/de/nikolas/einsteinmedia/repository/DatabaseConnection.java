package de.nikolas.einsteinmedia.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseConnection {

  private String host, database, user, password;
  private int port;

  private Connection connection;
  private ExecutorService executor;

  public DatabaseConnection(String host, String database, String user, String password, int port) {
    this.host = host;
    this.database = database;
    this.user = user;
    this.password = password;
    this.port = port;

    this.executor = Executors.newCachedThreadPool();
  }

  public void update(PreparedStatement statement) {
    if (this.isConnected()) {
      this.executor.execute(() -> this.queryUpdate(statement));
    } else {
      connect();
      update(statement);
    }
  }

  public void update(String statement) {
    if (this.isConnected()) {
      this.executor.execute(() -> this.queryUpdate(statement));
    } else {
      connect();
      update(statement);
    }
  }

  public ResultSet asyncQuery(PreparedStatement statement) {
    if (this.isConnected()) {
      Future<ResultSet> future =
          this.executor.submit(
              new Callable<ResultSet>() {

                @Override
                public ResultSet call() throws Exception {
                  return query(statement);
                }
              });
      try {
        return future.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }

    } else {
      connect();
      asyncQuery(statement);
    }
    return null;
  }

  public ResultSet asyncQuery(String statement) {
    if (this.isConnected()) {
      Future<ResultSet> future =
          this.executor.submit(
              new Callable<ResultSet>() {

                @Override
                public ResultSet call() throws Exception {
                  return query(statement);
                }
              });
      try {
        return future.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }

    } else {
      connect();
      asyncQuery(statement);
    }
    return null;
  }

  public PreparedStatement prepare(String query) {
    if (this.isConnected()) {
      try {
        return this.getConnection().prepareStatement(query);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      connect();
      prepare(query);
    }
    return null;
  }

  public void queryUpdate(String query) {
    if (this.isConnected()) {
      try (PreparedStatement statment = this.connection.prepareStatement(query)) {
        this.queryUpdate(statment);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else {
      connect();
      queryUpdate(query);
    }
  }

  public ResultSet query(String query) {
    if (this.isConnected()) {
      try {
        return this.query(this.connection.prepareStatement(query));
      } catch (SQLException e) {
        e.printStackTrace();
      }

    } else {
      connect();
      query(query);
    }
    return null;
  }

  public void queryUpdate(PreparedStatement statement) {
    if (this.isConnected()) {
      try {
        statement.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      connect();
      queryUpdate(statement);
    }
  }

  public ResultSet query(PreparedStatement statement) {
    if (this.isConnected()) {
      try {
        return statement.executeQuery();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      connect();
      query(statement);
    }
    return null;
  }

  public boolean isConnected() {
    try {
      if (this.connection == null || !this.connection.isValid(10) || this.connection.isClosed()) {
        return false;
      }
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  public void connect() {
    try {
      this.connection =
          DriverManager.getConnection(
              "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=utf8&useSSL=false&useUnicode=true" + database, user, password);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Connection getConnection() {
    if (this.isConnected()) {
      return connection;
    }
    return null;
  }

  public void closeConnection() {
    if (this.isConnected()) {
      try {
        this.connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        this.connection = null;
      }
    }
  }
}
