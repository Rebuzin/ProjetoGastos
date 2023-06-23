package com.example.projetoteste;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetoteste.models.Item;
import com.example.projetoteste.models.Cliente;
import com.example.projetoteste.models.Pedido;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private EditText edNome;
    private EditText edCpf;
    private EditText edItem;

    private EditText edVl_Unit;
    private EditText edVl_Tot;

    private EditText edQtn;

    private EditText edParcelas;
    private EditText edCodPed;
    private TextView tvListaItens;
    private TextView tvParcelas;
    private TextView tvListaParc;
    private LinearLayout layoutParcelas;

    private Button btSalvar;
    private Button btAdc;
    private Button btCalc;
    private Button btProc;
    private RadioGroup radioGroupPagamento;
    private RadioButton radioAVista;
    private RadioButton radioAPrazo;

    private ArrayList<Item> listItens = new ArrayList<Item>();
    private ArrayList<Pedido> listPedidos = new ArrayList<Pedido>();
    private double total = 0.0;

    private double totalFmPagto = 0.0;
    private int idCdPed = 1;
    private int nrParc;

    private boolean vista;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edNome = findViewById(R.id.edNome);
        edCpf = findViewById(R.id.edCpf);
        edItem = findViewById(R.id.edItem);
        edQtn = findViewById(R.id.edQtn);
        edVl_Unit = findViewById(R.id.edVl_Unit);
        edVl_Tot = findViewById(R.id.edVl_Tot);
        edParcelas = findViewById(R.id.edtParcelas);
        edCodPed = findViewById(R.id.edCd_Ped);
        layoutParcelas= findViewById(R.id.layoutParcelas);
        tvParcelas = findViewById(R.id.tvParcelas);
        tvListaItens = findViewById(R.id.tvListaItens);
        tvListaParc = findViewById(R.id.tvListaParc);
        btSalvar = findViewById(R.id.btSalvar);
        btAdc = findViewById(R.id.btAdiciona);
        btCalc = findViewById(R.id.btCalc);
        btProc = findViewById(R.id.btCdPed);

        radioGroupPagamento = findViewById(R.id.radioGroupPagamento);
        radioAVista = findViewById(R.id.radioAVista);
        radioAPrazo = findViewById(R.id.radioAPrazo);

        radioGroupPagamento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioAVista) {
                    layoutParcelas.setVisibility(View.GONE);
                    tvParcelas.setVisibility(View.GONE);
                    totalFmPagto = total - (total * 0.05);
                    edVl_Tot.setText(String.valueOf(totalFmPagto));
                } else if (checkedId == R.id.radioAPrazo) {
                    layoutParcelas.setVisibility(View.VISIBLE);
                    tvParcelas.setVisibility(View.VISIBLE);
                    totalFmPagto = total + (total * 0.05);
                    edVl_Tot.setText(String.valueOf(totalFmPagto));
                }
            }
        });


        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarPedido();
            }
        });

        btAdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionaItem();
            }
        });

        btCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcPrazo();
            }
        });

        btProc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {ProcurarPedido();
            }
        });

    }

    private void salvarPedido(){
        String nome = edNome.getText().toString();
        String cpf = edCpf.getText().toString();

        try{
            if(!nome.equals("") && !cpf.equals("") && !(listItens.size()==0) && (radioAVista.isChecked() || radioAPrazo.isChecked())){
                Cliente cliente = new Cliente(nome,cpf);
                if(radioAVista.isChecked()){
                    vista = true;
                }else{
                    vista = false;
                }
                Pedido pedido = new Pedido(idCdPed,cliente,listItens,totalFmPagto,vista);
                listPedidos.add(pedido);
                edNome.setText("");
                edCpf.setText("");
                edItem.setText("");
                edQtn.setText("0");
                edVl_Unit.setText("0");
                tvListaItens.setText("");
                radioAVista.setChecked(false);
                radioAPrazo.setChecked(false);
                edParcelas.setText("0");
                tvListaParc.setText("");
                edVl_Tot.setText("0");
                total = 0;
                idCdPed++;
                edCodPed.setText(String.valueOf(idCdPed));
                Toast.makeText(this, "Pedido de código "+(idCdPed-1)+" foi cadastrado com sucesso!", Toast.LENGTH_LONG).show();



            }else{
                if(nome.equals("")){
                    edNome.setError("Informe o Nome!");
                }
                if(cpf.equals("")){
                    edCpf.setError("Informe o CPF!");
                }
                if(radioAVista.isChecked() == false && radioAPrazo.isChecked() == false){
                    Toast.makeText(this, "Favor escolher uma forma de pagamento!", Toast.LENGTH_LONG).show();
                }
                if(listItens.size()==0){
                    Toast.makeText(this, "Favor inserir pelo menos um item a lista de pedido!", Toast.LENGTH_LONG).show();
                }
            }

        }catch(Exception ex){
            Log.e("ERRO", ex.getMessage());
        }
    }

    private void ProcurarPedido() {
        boolean existe = false;
        int cdExiste = 0;

        Integer cdPed = Integer.parseInt(edCodPed.getText().toString());

        if (cdPed.equals("")) {
            edCodPed.setText(idCdPed);
        }

        for(int i = 0; i<listPedidos.size();i++){
            if(cdPed.equals(listPedidos.get(i).getId())) {
                existe = true;
                cdExiste = i;
                break;
            }
        }

        try {
            String resultado = "";
            if (!cdPed.equals("") && !cdPed.equals(idCdPed)) {

                edNome.setText(listPedidos.get(cdExiste).getCliente().getNome());
                edCpf.setText(listPedidos.get(cdExiste).getCliente().getCpf());
                for (int i = 0; i<listPedidos.get(cdExiste).getListItens().size();i++) {
                    resultado += "Item: "+listPedidos.get(cdExiste).getListItens().get(i).getNome()+" Quantidade: "+listPedidos.get(cdExiste).getListItens().get(i).getQtn()+" Valor Unitário: "+listPedidos.get(cdExiste).getListItens().get(i).getVl_unitario()+"\n";
                }
                tvListaItens.setText(resultado);
                if (listPedidos.get(cdExiste).isVista()){
                    radioAVista.setChecked(true);
                }else{
                    Toast.makeText(this, "prazo", Toast.LENGTH_SHORT).show();
                    radioAPrazo.setChecked(true);
                    edParcelas.setText(nrParc);
                    resultado += "--------------------------------------"+ "\n";
                    double vlTotParc = (total + (total * 0.05))/nrParc;
                    for(int i = 0;i<=nrParc;i++){
                        resultado += "Parcela "+(i+1)+"° custa: R$ "+vlTotParc+"\n";
                    }
                    resultado += "--------------------------------------"+ "\n";
                    tvListaParc.setText(resultado);
                }
                total = listPedidos.get(cdExiste).getVlTotalPagto();
                edVl_Tot.setText(Double.toString(listPedidos.get(cdExiste).getVlTotalPagto()));


            } else {

                if (cdPed <= 0) {
                    edItem.setError("Informe um código válido!");
                }
                if (existe == false) {
                    Toast.makeText(this, "Não existe nenhum pedido com código "+cdPed, Toast.LENGTH_SHORT).show();
                }
            }

        }catch(Exception ex){
            Log.e("ERRO", ex.getMessage());
        }
    }

    private void adicionaItem(){
        if(edVl_Unit.getText().toString().equals("")){
            edVl_Unit.setText("0");
        }
        if(edQtn.getText().toString().equals("")){
            edQtn.setText("0");
        }

        String nm_item = edItem.getText().toString();
        Double vl_Unit = Double.parseDouble(edVl_Unit.getText().toString());
        Integer qtn = Integer.parseInt(edQtn.getText().toString());

        try{
            String resultado = tvListaItens.getText().toString();
            if(!nm_item.equals("") && !vl_Unit.equals("0")&& !qtn.equals("0")){
                resultado += "Item: "+nm_item+" Quantidade: "+qtn+" Valor Unitário: "+vl_Unit+"\n";
                tvListaItens.setText(resultado);
                Item item = new Item(nm_item,qtn,vl_Unit);
                listItens.add(item);
                total = 0.0;
                for (int i=0;i<listItens.size();i++){
                    total += listItens.get(i).getTotal();
                }
                edVl_Tot.setText(String.valueOf(total));
                Toast.makeText(this, "Item adicionado a lista com sucesso!", Toast.LENGTH_LONG).show();
            }else{
                if(nm_item.equals("")){
                    edItem.setError("Informe o nome do item!");
                }
                if(String.valueOf(qtn).startsWith("0")&& !qtn.equals("0")){
                    edQtn.setError("Informe a quantidade do item!");
                }
                if(String.valueOf(vl_Unit).startsWith("0")&& !vl_Unit.equals("0")){
                    edVl_Unit.setError("Informe o valor unitário!");
                }
            }

        }catch(Exception ex){
            Log.e("ERRO", ex.getMessage());
        }
    }

    private void calcPrazo(){

        if(edParcelas.getText().toString().equals("")){
            edParcelas.setText("0");
        }

        Integer qtnParc = Integer.parseInt(edParcelas.getText().toString());

        try{
            String resultado = tvListaParc.getText().toString();
            if(!qtnParc.equals("0")){
                double vlTotParc = (total + (total * 0.05))/qtnParc;
                resultado += "--------------------------------------"+ "\n";
                for(int i = 1;i<=qtnParc;i++){
                    resultado += "Parcela "+i+"° custa: R$ "+vlTotParc+"\n";
                }
                resultado += "--------------------------------------"+ "\n";
                tvListaParc.setText(resultado);
                nrParc = qtnParc;
            }else{
                if(listItens.size()==0){
                    Toast.makeText(this, "Favor inserir pelo menos um item a lista de pedido!", Toast.LENGTH_LONG).show();
                }
                if(String.valueOf(qtnParc).startsWith("0")&& !qtnParc.equals("0")){
                    edQtn.setError("Informe a quantidade de parcelas!");
                }
            }

        }catch(Exception ex){
            Log.e("ERRO", ex.getMessage());
        }
    }
}