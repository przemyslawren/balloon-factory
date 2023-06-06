import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Visual Layer
public class BalloonFactory extends JFrame {
    private final Storage storage = new Storage();
    private final List<Factory> factoryList = new ArrayList<>();
    private final List<Transporter> transporterList = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BalloonFactory::new);
    }

    public JPanel createFactory(JPanel factories) {
        JPanel factoryPanel = new JPanel();
        factoryPanel.setLayout(new BoxLayout(factoryPanel, BoxLayout.Y_AXIS));
        factoryPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        factoryPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        factoryPanel.setPreferredSize(new Dimension(200, 200));

        JLabel factoryLabel = new JLabel("Factory");
        Factory factory = new Factory(storage);
        factoryList.add(factory);
        JLabel producedLabel = new JLabel("Produced: 0");
        Thread producedThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                producedLabel.setText("Produced: " + factory.getProduced());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    factory.interrupt();
                    Thread.currentThread().interrupt();
                }
            }
        });
        producedThread.start();

        factoryPanel.add(factoryLabel);
        factoryPanel.add(producedLabel);

        JPanel sliderValuePanel = new JPanel();
        JSlider slider = new JSlider();
        slider.setMinimum(100);
        slider.setMaximum(3000);
        slider.setValue(factory.getFrequency());
        JLabel value = new JLabel(String.valueOf(slider.getValue()));
        slider.addChangeListener((e) -> {
            value.setText(String.valueOf(slider.getValue()));
            factory.setFrequency(slider.getValue());
        });
        sliderValuePanel.add(slider);
        sliderValuePanel.add(value);
        factoryPanel.add(sliderValuePanel);

        JButton deleteButton = new JButton("Delete factory");
        deleteButton.addActionListener(e -> {
            factoryList.remove(factory);
            factories.remove(factoryPanel);
            producedThread.interrupt();
            factory.interrupt();
            this.pack();
            this.repaint();
        });
        factoryPanel.add(deleteButton);

        return factoryPanel;
    }

    private synchronized JPanel createTransporter() {
        JPanel transporterPanel = new JPanel();
        transporterPanel.setLayout(new BoxLayout(transporterPanel, BoxLayout.Y_AXIS));
        transporterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        transporterPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel transporterLabel = new JLabel("Transporter");
        transporterPanel.add(transporterLabel);

        Transporter transporter = new Transporter(storage);
        transporterList.add(transporter);
        JLabel statusLabel = new JLabel(transporter.getStatus());
        Thread statusThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                statusLabel.setText(transporter.getStatus());
                this.repaint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        statusThread.start();
        transporterPanel.add(statusLabel);

        JButton startStopButton = new JButton("Stop");
        transporterPanel.add(startStopButton);
        transporterPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        startStopButton.addActionListener(e -> {
            if (startStopButton.getText().equals("Stop")) {
                startStopButton.setText("Start");
                transporter.pauseThread();
                transporter.setStatus("Stopped");
            } else {
                startStopButton.setText("Stop");
                transporter.resumeThread();
            }
        });

        return transporterPanel;
    }

    public BalloonFactory() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel factories = new JPanel();
        factories.setLayout(new BoxLayout(factories, BoxLayout.X_AXIS));
        factories.setAlignmentX(Component.CENTER_ALIGNMENT);
        factories.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setLayout(new BoxLayout(createButtonPanel, BoxLayout.X_AXIS));
        createButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButtonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        JButton createButton = new JButton("Create factory");
        createButton.addActionListener(el -> {
            factories.add(createFactory(factories));
            factories.remove(createButton);
            factories.add(createButton);
            this.pack();
        });
        createButtonPanel.add(createButton);
        factories.add(createButtonPanel);

        JScrollPane scrollFactories = new JScrollPane(factories, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollFactories.setPreferredSize(new Dimension(450, 110));

        JPanel storagePanel = new JPanel();
        storagePanel.setLayout(new GridLayout(10, 2));
        storagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        storagePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        storagePanel.setPreferredSize(new Dimension(500, 300));
        List<JLabel> storageLabels = new ArrayList<>();
        List<JPanel> colorPanelList = new ArrayList<>();
        for (int i = 0; i < storage.getMaxCapacity(); i++) {
            storageLabels.add(new JLabel(""));
            colorPanelList.add(new JPanel());
        }

        Thread storageThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                List<Balloon> copyStorage = storage.getStorage();
                for (int i = 0; i < copyStorage.size(); i++) {
                    if (i >= storageLabels.size()) {
                        break;
                    }
                    Balloon balloon = copyStorage.get(i);
                    if (balloon == null) {
                        storageLabels.get(i).setText("  ");
                        colorPanelList.get(i).setBackground(null);
                    } else {
                        storageLabels.get(i).setText(String.valueOf(balloon.getNumber()));
                        try {
                            colorPanelList.get(i).setBackground(balloon.getColor());
                        } catch (NullPointerException e) {
                            colorPanelList.get(i).setBackground(null);
                        }
                    }
                }
                this.repaint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        storageThread.start();

        for (int i = 0; i < storageLabels.size(); i++) {
            JPanel storageColorPanel = new JPanel();
            storageColorPanel.add(storageLabels.get(i));
            storageColorPanel.add(colorPanelList.get(i));

            storagePanel.add(storageColorPanel);
            this.pack();
        }

        JPanel transporters = new JPanel();
        transporters.setLayout(new BoxLayout(transporters, BoxLayout.X_AXIS));
        transporters.setAlignmentX(Component.CENTER_ALIGNMENT);
        transporters.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel createTransporterPanel = new JPanel();
        createTransporterPanel.setLayout(new BoxLayout(createTransporterPanel, BoxLayout.X_AXIS));
        createTransporterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        createTransporterPanel.setAlignmentY(Component.CENTER_ALIGNMENT);


        JButton createTransporterButton = new JButton("Create transporter");
        createTransporterButton.addActionListener(e -> {
            transporters.add(createTransporter());
            transporters.remove(createTransporterButton);
            transporters.add(createTransporterButton);
            this.pack();
            this.repaint();
        });
        createTransporterPanel.add(createTransporterButton);

        transporters.add(createTransporterPanel);

        JScrollPane scrollTransporters = new JScrollPane(transporters, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollTransporters.setPreferredSize(new Dimension(450, 110));

        mainPanel.add(scrollFactories);
        mainPanel.add(storagePanel);
        mainPanel.add(scrollTransporters);

        Thread overloadThread = new Thread(() -> {
            BalloonOverload overloadCanvas = new BalloonOverload(this);
            overloadCanvas.setPreferredSize(new Dimension(1400, 300));
            while (!Thread.currentThread().isInterrupted()) {
                if (storage.getStorage().size() >= storage.getMaxCapacity() - 10) {
                    for (Factory e : factoryList) {
                        e.pauseThread();
                    }
                    for (Transporter e : transporterList) {
                        e.pauseThread();
                    }

                    this.remove(mainPanel);
                    this.add(overloadCanvas);
                    this.pack();

                    storage.clear();
                    this.repaint();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    this.remove(overloadCanvas);
                    this.add(mainPanel);
                    this.pack();

                    for (Factory e : factoryList) {
                        e.resumeThread();
                    }
                    for (Transporter e : transporterList) {
                        if (!e.getStatus().equals("Stopped")) {
                            e.resumeThread();
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        overloadThread.start();

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Balloon Factory");
    }
}

