//package com.weisd.view;
//
//import java.util.ArrayList;
//
//
//
//public class VprograssDialogActivity extends ListActivity {
//	private ProgressDialog m_ProgressDialog = null; 
//    private ArrayList m_orders = null; 
//    private OrderAdapter m_adapter; 
//    private Runnable viewOrders; 
//    
//    @Override 
//    public void onCreate(Bundle savedInstanceState) { 
//        super.onCreate(savedInstanceState); 
//        setContentView(R.layout.main); 
//        m_orders = new ArrayList(); 
//        this.m_adapter = new OrderAdapter(this, R.layout.row, m_orders); 
//        
//        setListAdapter(this.m_adapter); 
//         
//        viewOrders = new Runnable(){ 
//            @Override 
//            public void run() { 
//                getOrders(); 
//            } 
//        }; 
//        Thread thread =  new Thread(null, viewOrders, "MagentoBackground"); 
//        thread.start(); 
//        m_ProgressDialog = ProgressDialog.show(VprograssDialogActivity.this,    
//              "Please wait...", "Retrieving data ...", true); 
//    } 
//    private Runnable returnRes = new Runnable() { 
//
//        @Override 
//        public void run() { 
//            if(m_orders != null && m_orders.size() > 0){ 
//                m_adapter.notifyDataSetChanged(); 
//                for(int i=0;i<m_orders.size();i++) 
//                m_adapter.add(m_orders.get(i)); 
//            } 
//            m_ProgressDialog.dismiss(); 
//            m_adapter.notifyDataSetChanged(); 
//        } 
//    }; 
//    private void getOrders(){ 
//          try{ 
//              m_orders = new ArrayList(); 
//              Order o1 = new Order(); 
//              o1.setOrderName("SF services"); 
//              o1.setOrderStatus("Pending"); 
//              Order o2 = new Order(); 
//              o2.setOrderName("SF Advertisement"); 
//              o2.setOrderStatus("Completed"); 
//              m_orders.add(o1); 
//              m_orders.add(o2); 
//              Thread.sleep(5000); 
//              Log.i("ARRAY", ""+ m_orders.size()); 
//            } catch (Exception e) { 
//              Log.e("BACKGROUND_PROC", e.getMessage()); 
//            } 
//            runOnUiThread(returnRes); 
//        } 
//    private class OrderAdapter extends ArrayAdapter{ 
//
//        private ArrayList items; 
//
//        public OrderAdapter(Context context, int textViewResourceId, ArrayList items) { 
//                super(context, textViewResourceId, items); 
//                this.items = items; 
//        } 
//        @Override 
//        public View getView(int position, View convertView, ViewGroup parent) { 
//                View v = convertView; 
//                if (v == null) { 
//                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
//                    v = vi.inflate(R.layout.row, null); 
//                } 
//                Order o = (Order) items.get(position); 
//                if (o != null) { 
//                        TextView tt = (TextView) v.findViewById(R.id.toptext); 
//                        TextView bt = (TextView) v.findViewById(R.id.bottomtext); 
//                        if (tt != null) { 
//                              tt.setText("Name: "+o.getOrderName());                            } 
//                        if(bt != null){ 
//                              bt.setText("Status: "+ o.getOrderStatus()); 
//                        } 
//                } 
//                return v; 
//        } 
//} 
//} 
