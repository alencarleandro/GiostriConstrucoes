export const products = [
    {
      id: 1,
      name: "Cimento CP II 50kg",
      brand: "Holcim",
      price: 29.90,
      category: "Básicos",
      image: "https://images.unsplash.com/photo-1518709766631-a6a7f45921c3?w=500",
      stock: 100
    },
    {
      id: 2,
      name: "Tijolo Cerâmico 8 Furos",
      brand: "Cerâmica Real",
      price: 1.20,
      category: "Alvenaria",
      image: "https://images.unsplash.com/photo-1590937893289-2c29b8827709?w=500",
      stock: 5000
    },
    {
      id: 3,
      name: "Areia Média m³",
      brand: "Giostri",
      price: 120.00,
      category: "Básicos",
      image: "https://images.unsplash.com/photo-1589939705384-5185137a7f0f?w=500",
      stock: 50
    }
  ];
  
  export const orders = [
    {
      id: "PED001",
      date: "2024-01-15",
      status: "Entregue",
      total: 599.80,
      items: [
        { productId: 1, quantity: 20, price: 29.90 }
      ]
    },
    {
      id: "PED002",
      date: "2024-01-20",
      status: "Em separação",
      total: 720.00,
      items: [
        { productId: 3, quantity: 6, price: 120.00 }
      ]
    }
  ];
  
  export const categories = [
    "Básicos",
    "Alvenaria",
    "Hidráulica",
    "Elétrica",
    "Ferramentas",
    "Acabamento",
    "Pintura",
    "Portas e Janelas"
  ];
  
  export const user = {
    id: 1,
    name: "João Silva",
    email: "joao.silva@email.com",
    phone: "(31) 99999-9999",
    addresses: [
      {
        id: 1,
        street: "Rua dos Construtores",
        number: "123",
        complement: "Casa",
        neighborhood: "Centro",
        city: "Belo Horizonte",
        state: "MG",
        zipCode: "30100-000",
        isDefault: true
      }
    ]
  };
  
  export const favorites = [1, 3];